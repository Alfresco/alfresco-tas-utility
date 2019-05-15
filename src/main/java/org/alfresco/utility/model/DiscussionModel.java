package org.alfresco.utility.model;

import java.util.List;

/**
 * Created by Claudia Agache on 11/18/2016.
 */
public class DiscussionModel extends TestModel
{
    private String title;
    private String text;
    private String nodeRef;
    private List<String> tags;

    public DiscussionModel(String title, String text,  List<String> tags, String nodeRef)
    {
        this.title = title;
        this.text = text;
        this.tags = tags;
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

    public String getDescription()
    {
        return text;
    }

    public void setDescription(String description)
    {
        this.text = description;
    }

    public String getNodeRef()
    {
        return nodeRef;
    }

    public void setNodeRef(String nodeRef)
    {
        this.nodeRef = nodeRef;
    }

    public List<String> getTags()
    {
        return tags;
    }

    public void setTags(List<String> tags)
    {
        this.tags = tags;
    }


}
