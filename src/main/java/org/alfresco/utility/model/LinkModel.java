package org.alfresco.utility.model;

/**
 * Created by Claudia Agache on 11/18/2016.
 */
public class LinkModel extends TestModel
{
    private String title;
    private String url;
    private String description;
    private boolean internal;
    private String nodeRef;

    public LinkModel(String title, String url, String description, boolean internal, String nodeRef)
    {
        this.title = title;
        this.url = url;
        this.description = description;
        this.internal = internal;
        this.nodeRef = nodeRef;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public boolean getInternal()
    {
        return internal;
    }

    public void setInternal(boolean internal)
    {
        this.internal = internal;
    }

    public String getNodeRef()
    {
        return nodeRef;
    }

    public void setNodeRef(String nodeRef)
    {
        this.nodeRef = nodeRef;
    }


}
