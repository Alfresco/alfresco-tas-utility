package org.alfresco.utility.data.node;

import java.util.List;

public interface Node
{
    public Node addFolder(String folderName);

    public Node addFile(String fileName);

    public List<Node> getNodes();

    public void setNodeName(String nodeName);

    public String getNodeName();

    public String getFullPath();
}
