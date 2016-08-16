package org.alfresco.utility.model;

import java.io.File;
import java.nio.file.Paths;

import org.apache.commons.lang3.RandomStringUtils;

public class FileModel extends ContentModel
{
    private String content;
    private FileType fileType;

    public FileModel(File location)
    {
        super(location);
        setFileType(FileType.fromPath(location.getPath()));
    }

    public FileModel(FileType fileType, File location)
    {
        super(location);
        setFileType(fileType);
    }

    public FileModel(FileType fileType, File location, String tile)
    {
        super(location, tile);
        setFileType(fileType);
    }

    public FileModel(FileType fileType, File location, String title, String description)
    {
        super(location, title, description);
        setFileType(fileType);
    }

    public FileModel(FileType fileType, File location, String title, String description, String content)
    {
        this(fileType, location, title, description);
        setContent(content);
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public FileType getFileType()
    {
        return fileType;
    }

    public void setFileType(FileType fileType)
    {
        this.fileType = fileType;
    }

    public static FileModel getRandomFileModel(FileType fileType, String parent)
    {
        File location = Paths.get(parent, String.format("file-%s.%s", RandomStringUtils.randomAlphanumeric(10), fileType.extention)).toFile();
        FileModel newFile = new FileModel(location);
        return newFile;
    }
}
