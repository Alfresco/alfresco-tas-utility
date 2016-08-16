package org.alfresco.utility.dsl;

import org.alfresco.utility.model.FolderModel;

public interface DSLCrudFolder<Protocol>
{
    public Protocol createFolder(String fullPath) throws Exception;

    public Protocol createFolder(FolderModel folderModel) throws Exception;

    public Protocol renameFolder(String fullPath) throws Exception;

    public Protocol renameFolder(FolderModel folderModel) throws Exception;

    public Protocol deleteFolder(String fullPath) throws Exception;

    public Protocol deleteFolder(FolderModel folderModel) throws Exception;

    public Protocol copyFolder(String sourceFullPath, String destinationFullPath) throws Exception;

    public Protocol copyFolder(FolderModel source, FolderModel destination) throws Exception;

    public Protocol moveFolder(String sourceFullPath, String destinationFullPath) throws Exception;

    public Protocol moveFolder(FolderModel source, String destination) throws Exception;
}
