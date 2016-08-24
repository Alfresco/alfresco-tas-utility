package org.alfresco.utility.data.node;

import java.util.List;

import javax.annotation.PostConstruct;

import org.alfresco.utility.data.node.action.Action;
import org.alfresco.utility.data.node.action.ActionBase;
import org.alfresco.utility.data.node.action.NodeAction;
import org.alfresco.utility.dsl.DSLFile;
import org.alfresco.utility.exception.TestConfigurationException;

public abstract class RepositoryNodesBuilder extends NodeBase
{
    public RepositoryNodesBuilder(String name)
    {
        super("", new FolderNode(name, null));
    }

    public void createStructure() throws Exception
    {
        checkClientInitialization();
        traverseChildren(getNodes());
    }

    public void traverseChildren(List<Node> nodes) throws Exception
    {
        for (Node n : nodes)
        {
            if (n instanceof NodeAction)
            {
                NodeAction na = (NodeAction) n;
                na.doWork();
            }
            traverseChildren(n.getNodes());
        }
    }

    @PostConstruct
    public abstract void registerActions();

    public void useClient(DSLFile<?> client)
    {
        for (Action action : getActions())
        {
            ActionBase ab = (ActionBase) action;
            ab.setClient(client);
        }
    }

    private void checkClientInitialization() throws TestConfigurationException
    {
        ActionBase ab = (ActionBase) getActions().get(getActions().size() - 1);
        if (ab.getClient() == null)
            throw new TestConfigurationException("You didn't specify your DSLCLient. Call your custom RepositoryBuilder#useClient(DSLClient<?> client)' first.");
    }
}
