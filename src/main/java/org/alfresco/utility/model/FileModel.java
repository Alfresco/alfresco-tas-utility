package org.alfresco.utility.model;

import java.io.File;

import com.google.common.io.Files;

public class FileModel extends TestModel
{
    private File path;
    private String title;

    public FileModel(File path, String tile)
    {
        setPath(path);
        setTitle(tile);
    }

    public FileModel(File path)
    {
        setPath(path);
        setTitle(Files.getNameWithoutExtension(path.getName()));
    }

    public File getPath()
    {
        return path;
    }

    public void setPath(File path)
    {
        this.path = path;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }

    public String getName()
    {
        return getPath().getName();
    }
}
