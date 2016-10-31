package org.alfresco.utility.data.provider;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.alfresco.utility.model.TagModel;

@XmlType(name = "tag")
public class XMLTagData implements XMLDataItem
{
    private String name;
    private String createdBy;
    
    @Override
    public TagModel getModel()
    {
        TagModel model = new TagModel();
        model.setTag(name);
        return model;
    }
    
    @XmlAttribute(name = "name")
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @XmlAttribute(name = "createdBy")
    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }

}
