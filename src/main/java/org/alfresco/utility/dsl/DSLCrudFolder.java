package org.alfresco.utility.dsl;

public interface DSLCrudFolder<Protocol>
{
    public Protocol createFolder(String fullPath) throws Exception;

    public Protocol renameFolder(String fullPath) throws Exception;

    public Protocol deleteFolder() throws Exception;

    public Protocol copyFolder(String sourceFullPath, String destinationFullPath) throws Exception;

    public Protocol moveFolder(String sourceFullPath, String destinationFullPath) throws Exception;
}
