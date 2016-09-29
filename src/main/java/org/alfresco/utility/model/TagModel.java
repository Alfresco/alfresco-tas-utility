package org.alfresco.utility.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TagModel extends TestModel
{
    @JsonProperty(required = true)
    protected String tag;

    @JsonProperty(required = true)
    protected String id;

    public TagModel()
    {
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTag()
    {
        return tag;
    }

    public void setTag(String tag)
    {
        this.tag = tag;
    }

}