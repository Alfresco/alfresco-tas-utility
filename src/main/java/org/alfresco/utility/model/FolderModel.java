package org.alfresco.utility.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FolderModel extends ContentModel
{
    List<FileModel> files = new ArrayList<FileModel>();

    public FolderModel(String name)
    {
        super(name);
    }

    public FolderModel(File location)
    {
        super(location);
    }

    public FolderModel(File location, String title)
    {
        super(location, title);
    }

    public FolderModel(File location, String title, String description)
    {
        super(location, title, description);
    }

    /**
     * Generate a new random FileModel object
     * 
     * @param fileType
     * @return FileModel inside this folder
     */
    public FolderModel generateRandomFileChild(FileType fileType)
    {
        addFileAsChild(FileModel.getRandomFileModel(fileType, getLocation()));
        return this;
    }

    public FolderModel addFileAsChild(FileModel fileModel)
    {
        getFiles().add(fileModel);
        return this;
    }

    public List<FileModel> getFiles()
    {
        return files;
    }

    public FileModel lastFile()
    {
        return getFiles().get(getFiles().size() - 1);
    }

}
