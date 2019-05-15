package org.alfresco.utility.model;

import java.io.File;
import java.nio.file.Paths;

import javax.xml.bind.annotation.XmlAttribute;

import org.alfresco.utility.Utility;

public class ContentModel extends RepoTestModel
{
    private String name;
    private String title;
    private String description;
    private String cmisLocation;
    private String protocolLocation;

    public ContentModel()
    {
    }

    public ContentModel(String name)
    {
        setName(name);
        setCmisLocation(name);
    }

    public ContentModel rename(String newName) throws Exception
    {
        setName(newName);       
        setCmisLocation(Paths.get(Paths.get(getCmisLocation()).getParent().toString(), newName).toString());
        Utility.checkObjectIsInitialized(getProtocolLocation(), "getProtocolLocation");
        setProtocolLocation(Paths.get(Paths.get(getProtocolLocation()).getParent().toString(), newName).toString());
        return this;
    }

    public ContentModel(String name, String title, String description)
    {
        this(name);
        setTitle(title);
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

    @XmlAttribute(name = "name")
    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Get cmis path of the content
     * (e.g. /Sites/sitename/documentLibrary)
     * 
     * @return
     */
    public String getCmisLocation()
    {
        return Utility.convertBackslashToSlash(cmisLocation);
    }

    public void setCmisLocation(String cmisLocation)
    {
        this.cmisLocation = cmisLocation;
    }

    /**
     * Get path for a specific protocol
     * (e.g. AlfrescoIMAP/Sites/sitename/documentLibrary
     * Alfresco/Sites/sitename/documentLibrary)
     * 
     * @return
     */
    public String getProtocolLocation()
    {
        return protocolLocation;
    }

    public void setProtocolLocation(String protocolLocation)
    {
        this.protocolLocation = protocolLocation;
    }

    public String getParentFolder()
    {
        return new File(getCmisLocation()).getParent();
    }
    
    public String getProtocolParentFolder()
    {
        return new File(getProtocolLocation()).getParent();
    }
    
    /**
     * @return -my- 
     */
    public static ContentModel my()
    {
        ContentModel my = new ContentModel();
        my.setName("-my-");
        my.setNodeRef("-my-");
        return my;
    }
}
