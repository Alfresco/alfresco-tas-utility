package org.alfresco.utility.data;

import org.alfresco.utility.dsl.DSLProtocol;

/**
 * This will handle the last objects created in repository
 * On some protocols, the location of the content created has a prefixed keyword
 * Example: smb://<address>:<port>/Sites/....
 * In some cases we want to assert the existance of this resources using just the CMIS path
 * so /Sites/.... stripping the protocol prefix.
 * 
 * This class will do exactly this.
 * 
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
        return fullPath;
    }

    public String getPathWithoutPrefix()
    {
        return fullPath.replaceAll(protocol.getPrefixSpace(), "");
    }

    public void setFullPath(String lastData)
    {
        this.fullPath = lastData;
    }
}
