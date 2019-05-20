package org.alfresco.utility.model;

import javax.xml.bind.annotation.XmlAttribute;

import org.alfresco.dataprep.SiteService.Visibility;
import org.alfresco.utility.Utility;
import org.alfresco.utility.data.RandomData;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SiteModel extends TestModel
{
    @JsonProperty(required = true)
    protected Visibility visibility;

    @JsonProperty(required = true)
    protected String guid;

    @JsonProperty(required = true)
    protected String id;

    @JsonProperty(required = true)
    protected String title;

    protected String description;

    public SiteModel()
    {
    }

    public SiteModel(String title)
    {
        this(title, Visibility.PUBLIC);
    }
    
    public SiteModel(String title,Visibility visibility)
    {
        this.visibility = visibility;
        setTitle(title);
        setId(title);
        setDescription(String.format("%s%s", title, visibility));
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

    @XmlAttribute
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
    
    
    /**
     * Example: 25eb5ef8-50a5-4873-adc5-8edd87e628a4;1.0
     * will be returned as "25eb5ef8-50a5-4873-adc5-8edd87e628a4" without 1.0
     * @return {@link #getGuid() } without version number
     */
    public String getGuidWithoutVersion()
    {
        return Utility.splitGuidVersion(getGuid());
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

    public static SiteModel getRandomSiteModel()
    {
        String siteName = RandomData.getRandomName("site");
        LOG.info("Generating new random Site Model: {}", siteName);
        return new SiteModel(siteName);
    }
}
