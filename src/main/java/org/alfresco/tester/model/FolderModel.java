package org.alfresco.tester.model;

import java.io.File;

import com.google.common.io.Files;

public class FolderModel extends TestModel
{
    private String title;
    private String description;
    private File path;
    
    public FolderModel()
    {
    }

    public FolderModel(File path)
    {
        setPath(path);
        setTitle(Files.getNameWithoutExtension(path.getName()));
    }
    
    public FolderModel(File path, String tile)
    {
        setPath(path);
        setTitle(tile);
    }
    
    public FolderModel(File path, String title, String description)
    {
        this(path, title);
        setDescription(description);
    }
    
    public File getPath()
    {
        return path;
    }
 
    public void setPath(File path)
    {
        this.path = path;
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
}
