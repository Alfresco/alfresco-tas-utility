package org.alfresco.tester.model;

import java.io.File;

public class FolderModel extends TestModel
{
    private String title;
    private String description;
    private String name;
    private String cmisObjecTypeId = "cmis:folder";
    private File path;
    
    public FolderModel()
    {
    }
    
    public FolderModel(String name, String title, String description, File path)
    {
        setName(name);
        setTitle(title);
        setDescription(description);
        setPath(path);
    }
    
    public FolderModel(File path)
    {
        this.path = path;
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCmisObjecTypeId()
    {
        return cmisObjecTypeId;
    }

}
