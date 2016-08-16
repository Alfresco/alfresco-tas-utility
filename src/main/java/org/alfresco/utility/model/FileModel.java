package org.alfresco.utility.model;

import java.io.File;

import org.alfresco.utility.data.DataValue;

import com.google.common.io.Files;

public class FileModel extends TestModel
{
    private String title;
    private String description;
    private File fileLocation = new File(DataValue.UNDEFINED.name());
    private String content;

    private FileType fileType;

    public FileModel()
    {
    }

    public FileModel(FileType fileType, File fullPath)
    {
        setFileType(fileType);
        setPath(fullPath);
        setTitle(getName());
    }

    public FileModel(FileType fileType, File fullPath, String title, String description)
    {
        this(fileType, fullPath);
        setTitle(title);
        setDescription(description);
    }

    public FileModel(FileType fileType, File fullPath, String title, String description, String content)
    {
        this(fileType, fullPath, title, description);
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

    public String getName()
    {
        return Files.getNameWithoutExtension(this.fileLocation.getName());
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
        return fileLocation;
    }

    public String getLocation()
    {
        return getPath().getPath();
    }

    public void setPath(File path)
    {
        this.fileLocation = path;
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
