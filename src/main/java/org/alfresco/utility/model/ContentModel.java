package org.alfresco.utility.model;

import java.io.File;
import java.nio.file.Paths;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.alfresco.utility.Utility;

import com.google.common.io.Files;

public class ContentModel extends TestModel
{

    private String title;
    private String name;

    private String description;

    private File location;
    private String protocolLocation;

    private String path;

    public ContentModel()
    {

    }

    public ContentModel(String name)
    {
        setName(name);
        setLocation(Paths.get(name).toFile());
        setTitle(Files.getNameWithoutExtension(location.getName()));
    }

    public ContentModel(File location)
    {
        setName(location.getName());
        setLocation(location);
        setTitle(Files.getNameWithoutExtension(location.getName()));
    }

    public ContentModel(File location, String title)
    {
        setLocation(location);
        setTitle(title);
    }

    public ContentModel(File location, String title, String description)
    {
        this(location, title);
        setDescription(description);
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

    @XmlElement(name = "location")
    public String getLocation()
    {
        return Utility.convertBackslashToSlash(location.getPath());
    }

    public File getLocationPath()
    {
        return location;
    }

    public void setLocation(File location)
    {
        this.location = location;
        this.name = location.getName();
    }

    @XmlAttribute(name = "name")
    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getProtocolLocation()
    {
        return protocolLocation;
    }

    public void setProtocolLocation(String protocolLocation)
    {
        this.protocolLocation = protocolLocation;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

}
