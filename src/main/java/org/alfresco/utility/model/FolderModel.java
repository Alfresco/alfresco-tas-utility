package org.alfresco.utility.model;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

public class FolderModel extends ContentModel
{
    List<FileModel> files = new ArrayList<FileModel>();

    public FolderModel(File location)
    {
        super(location);
    }

    public FolderModel(File location, String tile)
    {
        super(location, tile);
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
    public FolderModel addRandomFile(FileType fileType)
    {
        File location = Paths.get(getLocation(), String.format("file-%s%s", RandomStringUtils.randomAlphanumeric(10), fileType.extention)).toFile();
        FileModel newFile = new FileModel(fileType, location);
        addFile(newFile);
        return this;
    }

    public FolderModel addFile(FileModel fileModel)
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
