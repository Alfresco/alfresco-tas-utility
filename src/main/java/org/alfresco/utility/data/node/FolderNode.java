package org.alfresco.utility.data.node;

import org.alfresco.utility.data.node.action.Action;
import org.alfresco.utility.data.node.action.FolderAction;
import org.alfresco.utility.data.node.action.NodeAction;

public class FolderNode extends NodeBase implements NodeAction
{
    public FolderNode(String name, Node parent)
    {
        super(name, parent);
    }

    @Override
    public void doWork() throws Exception
    {
        getFolderAction().createFolder(getFullPath());
    }

    private FolderAction getFolderAction()
    {
        for (Action action : getActions())
        {
            if (action instanceof FolderAction)
                return (FolderAction) action;
        }
        return null;
    }
}
