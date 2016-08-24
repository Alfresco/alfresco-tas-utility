package org.alfresco.utility.dsl;

import org.alfresco.utility.model.FolderModel;

public interface DSLCrud<Protocol> extends DSLFile<Protocol>, DSLFolder<Protocol>
{
    public Protocol rename(String newName) throws Exception;

    public Protocol update(String content) throws Exception;

    public Protocol delete() throws Exception;

    public Protocol copyTo(FolderModel destination) throws Exception;

    public Protocol moveTo(FolderModel destination) throws Exception;
}
