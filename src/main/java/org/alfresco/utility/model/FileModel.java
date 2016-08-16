package org.alfresco.utility.model;

import java.io.File;

import com.google.common.io.Files;

public class FileModel extends TestModel
{
    private String title;
    private String description;
    private File path;
    private String content;

    private FileType fileType;
    
    public FileModel()
    {
    }
    
    public FileModel(FileType fileType, File fullPath)
    {
        setFileType(fileType);
        setPath(path);
        setTitle(Files.getNameWithoutExtension(path.getName()));
    }
    
    public FileModel(FileType fileType, File fullPath, String title, String description)
    {
        this(fileType, fullPath);
        setTitle(title);
        setDescription(description);
    }
    
    public FileModel(FileType fileType, File fullPath, String title, String description, String content)
    {
        this(fileType, fullPath,title, description);
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

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public File getPath()
    {
        return path;
    }

    public void setPath(File path)
    {
        this.path = path;
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
