package org.alfresco.utility.dsl;

import org.alfresco.utility.model.FileModel;
import org.alfresco.utility.model.FolderModel;

public interface DSLCrudFile<Protocol>
{
    public Protocol createFile(String fullPath) throws Exception;

    public Protocol createFile(FileModel model) throws Exception;

    public Protocol renameFile(String fullPath, String newName) throws Exception;

    public Protocol renameFile(FileModel fileModel, String newName) throws Exception;

    public Protocol updateFile(String fullPath, String content) throws Exception;

    public Protocol updateFile(FileModel fileModel, String content) throws Exception;

    public Protocol deleteFile(String fullPath) throws Exception;

    public Protocol deleteFile(FileModel fileModel) throws Exception;

    public Protocol copyFile(String sourceFullPath, String destinationFullPath) throws Exception;

    public Protocol copyFile(FileModel fileModelSource, FolderModel destination) throws Exception;

    public Protocol moveFile(String sourceFullPath, String destinationFullPath) throws Exception;

    public Protocol moveFile(FileModel fileModelSource, FolderModel destination) throws Exception;
}