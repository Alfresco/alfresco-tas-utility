package org.alfresco.utility.dsl;

import java.util.List;

import org.alfresco.utility.model.FolderModel;

public interface DSLFolder<Protocol>
{
    public Protocol createFolder(FolderModel folderModel) throws Exception;

    public List<FolderModel> getFolders() throws Exception;
}
