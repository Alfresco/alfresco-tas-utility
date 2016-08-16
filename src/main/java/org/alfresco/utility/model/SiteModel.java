package org.alfresco.utility.model;

import org.springframework.social.alfresco.api.entities.Site.Visibility;

public class SiteModel extends TestModel
{    
    protected Visibility visibility;
    protected String guid = "no-guid";
    protected String id = "no-id";
    protected String title = "no-title";
    protected String description = "no-description";

    public SiteModel()
    {
    }
    
    public SiteModel(String title)
    {
        setTitle(title);
        setId(title);
    }

    public SiteModel(Visibility visibility, String guid, String id, String title, String description)
    {        
        this.visibility = visibility;
        this.guid = guid;
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

   

    public Visibility getVisibility()
    {
        return visibility;
    }

    public void setVisibility(Visibility visibility)
    {
        this.visibility = visibility;
    }

    public String getGuid()
    {
        return guid;
    }

    public void setGuid(String guid)
    {
        this.guid = guid;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

}
