package org.alfresco.utility.data.node;

import org.alfresco.utility.data.node.action.Action;
import org.alfresco.utility.data.node.action.FileAction;
import org.alfresco.utility.data.node.action.NodeAction;

public class FileNode extends NodeBase implements NodeAction
{
    public FileNode(String name, Node parent)
    {
        super(name, parent);
    }

    @Override
    public void doWork() throws Exception
    {
        getFileAction().createFile(getFullPath());
    }

    private FileAction getFileAction()
    {
        for (Action action : getActions())
        {
            if (action instanceof FileAction)
                return (FileAction) action;
        }
        return null;
    }

}
