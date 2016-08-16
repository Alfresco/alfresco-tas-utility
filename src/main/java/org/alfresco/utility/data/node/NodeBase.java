package org.alfresco.utility.data.node;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.alfresco.utility.LogFactory;
import org.alfresco.utility.data.node.action.Action;
import org.slf4j.Logger;

public class NodeBase implements Node
{
    static Logger LOG = LogFactory.getLogger();

    private String nodeName;
    private Node parent;
    private List<Node> nodes = new ArrayList<Node>();
    private Node lastFolderNode = null;
    private List<Action> actions = new ArrayList<Action>();

    public NodeBase(String name, Node parent)
    {
        setNodeName(name);
        setParent(parent);
        setLastFolderNode(parent);
    }

    @Override
    public void setNodeName(String nodeName)
    {
        this.nodeName = nodeName;
    }

    @Override
    public String getNodeName()
    {
        return nodeName;
    }

    @Override
    public Node addFolder(String folderName)
    {
        Node parent = ((this instanceof FileNode) ? getLastFolderNode() : this);
        FolderNode node = new FolderNode(folderName, parent);
        node.setActions(getActions());

        nodes.add(node);
        setLastFolderNode(getLastNode());
        return getLastNode();
    }

    @Override
    public Node addFile(String fileName)
    {
        Node parent = ((this instanceof FileNode) ? getLastFolderNode() : this);
        FileNode node = new FileNode(fileName, parent);
        node.setActions(getActions());
        nodes.add(node);

        return getLastNode();
    }

    public Node getLastNode()
    {
        return getNodes().get(getNodes().size() - 1);
    }

    @Override
    public List<Node> getNodes()
    {
        return nodes;
    }

    public String getFullPath()
    {
        if (parent == null)
        {
            return getNodeName();
        }
        else
            return Paths.get(parent.getFullPath(), getNodeName()).toString();
    }

    public Node getParent()
    {
        return parent;
    }

    private void setParent(Node parent)
    {
        this.parent = parent;
    }

    public String toString()
    {
        StringBuilder info = new StringBuilder("Content=").append(this.getClass().getSimpleName()).append("[");
        info.append("fullPath=").append(getFullPath()).append("]");
        return info.toString();
    }

    public Node getLastFolderNode()
    {
        return lastFolderNode;
    }

    public void setLastFolderNode(Node lastFolderNode)
    {
        this.lastFolderNode = lastFolderNode;
    }

    public List<Action> getActions()
    {
        return actions;
    }

    protected void addAction(Action action)
    {
        getActions().add(action);
    }

    protected void setActions(List<Action> actions)
    {
        this.actions = actions;
    }

}
