package org.alfresco.utility.model;

import java.io.File;

public class FileModel extends ContentModel
{
    private String content;
    private FileType fileType;

    public FileModel(FileType fileType, File fullPath, String title, String description, String content)
    {
        this(fileType, fullPath, title, description);
        setContent(content);
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
}
