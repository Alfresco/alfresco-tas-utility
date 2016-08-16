package org.alfresco.utility.model;

import java.io.File;

import org.alfresco.utility.data.DataValue;

import com.google.common.io.Files;

public class FolderModel extends TestModel
{
    private File folderLocation = new File(DataValue.UNDEFINED.name());
    private String description;
    private String title;

    public FolderModel(File path, String title, String description)
    {
        this(path, title);
        setDescription(description);
    }

    public FolderModel(File path, String tile)
    {
        setPath(path);
        setTitle(tile);
    }

    public FolderModel(File path)
    {
        setPath(path);
        setTitle(Files.getNameWithoutExtension(path.getName()));
    }

    public File getPath()
    {
        return folderLocation;
    }

    public void setPath(File path)
    {
        this.folderLocation = path;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getName()
    {
        return this.folderLocation.getName();
    }
    
    public String getLocation()
    {
        return getPath().getPath();
    }

}
