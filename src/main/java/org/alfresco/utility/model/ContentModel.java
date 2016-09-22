package org.alfresco.utility.model;

import javax.xml.bind.annotation.XmlAttribute;

import org.alfresco.utility.Utility;

public abstract class ContentModel extends TestModel
{
    private String name;
    private String title;
    private String description;
    private String cmisLocation;
    private String protocolLocation;

    public ContentModel(String name)
    {
        setName(name);
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
}
