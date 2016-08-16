package org.alfresco.utility.dsl;

/**
 * Protocol interface
 */
public interface DSLCrud<Protocol>
{
    public Protocol createFile(String fullPath) throws Exception;

    public Protocol renameFile(String fullPath) throws Exception;

    public Protocol deleteFile();

    public Protocol createFolder(String fullPath) throws Exception;

    public Protocol renameFolder(String fullPath) throws Exception;

    public Protocol deleteFolder() throws Exception;

    public Protocol copyFolder(String locationPath, String destinationPath) throws Exception;
       
}
