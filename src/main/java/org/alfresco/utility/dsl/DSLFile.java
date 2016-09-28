package org.alfresco.utility.dsl;

import java.util.List;

import org.alfresco.utility.model.FileModel;

public interface DSLFile<Protocol>
{
    public List<FileModel> getFiles() throws Exception;
    
    public Protocol createFile(FileModel fileModel) throws Exception;
}