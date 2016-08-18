package org.alfresco.utility.data;

import org.alfresco.utility.dsl.DSLProtocol;

/**
 * This will handle the last objects created in repository
 */
public class ResourceContent
{
    private DSLProtocol<?> protocol;
    private String fullPath = "";

    public ResourceContent(DSLProtocol<?> protocol)
    {
        this.protocol = protocol;
    }

    public String getFullPath()
    {
        return fullPath.replaceAll(protocol.getPrefixSpace(), "");
    }

    public String getOriginalFullPath()
    {
        return fullPath;
    }

    public void setFullPath(String lastData)
    {
        this.fullPath = lastData;
    }
}
