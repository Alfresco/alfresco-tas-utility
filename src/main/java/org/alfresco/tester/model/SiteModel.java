package org.alfresco.tester.model;

import org.springframework.social.alfresco.api.entities.Site.Visibility;

public class SiteModel
{
    private String role = "no-role";
    private Visibility visibility;
    private String guid = "no-guid";
    private String id = "no-id";
    private String title = "no-title";
    private String description = "no-description";

    public SiteModel()
    {
    }

    public SiteModel(String role, Visibility visibility, String guid, String id, String title, String description)
    {
        this.role = role;
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

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
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
