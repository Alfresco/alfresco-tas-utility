package org.alfresco.utility.dsl;

import org.alfresco.utility.model.ContentModel;

public interface DSLContentModelAction<Protocol>
{
    public Protocol rename(String newName) throws Exception;

    public Protocol update(String content) throws Exception;

    public Protocol delete() throws Exception;

    /**
     * Copy last resource content to destination.
     * The content to be copied will be set as last resource in the new location.
     */
    public Protocol copyTo(ContentModel destination) throws Exception;

    /**
     * Move last resource content to destination.
     * The content to be moved will be set as last resource in the new location.
     */
    public Protocol moveTo(ContentModel destination) throws Exception;
}
