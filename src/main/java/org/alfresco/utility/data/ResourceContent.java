package org.alfresco.utility.data;

import org.alfresco.utility.dsl.DSLWrapper;

/**
 * This will handle the last objects created in repository
 */
public class ResourceContent
{
    private DSLWrapper<?> clientWrapper;
    private String fullPath = "";

    public ResourceContent(DSLWrapper<?> clientWrapper)
    {
        this.clientWrapper = clientWrapper;
    }

    public String getFullPath()
    {
        return fullPath.replaceAll(clientWrapper.getRepositoryPrefixPath(), "");
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