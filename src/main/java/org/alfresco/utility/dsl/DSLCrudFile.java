package org.alfresco.utility.dsl;

public interface DSLCrudFile<Protocol>
{
    public Protocol createFile(String fullPath) throws Exception;

    public Protocol renameFile(String fullPath) throws Exception;

    public Protocol updateFile(String fullPath, String content) throws Exception;

    public Protocol deleteFile();

    public Protocol copyFile(String sourceFullPath, String destinationFullPath) throws Exception;

    public Protocol moveFile(String sourceFullPath, String destinationFullPath) throws Exception;
}