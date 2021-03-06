package org.alfresco.utility.data.provider;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.alfresco.utility.model.TagModel;

@XmlType(name = "tag")
public class XMLTagData implements XMLDataItem 
{
    private String value;
    private String createdBy;
    private String id;
    
    @Override
    public TagModel getModel()
    {
        TagModel model = new TagModel();
        model.setTag(value);
        return model;
    }
    
    @XmlAttribute(name = "value")
    public String getName()
    {
        return value;
    }

    public void setName(String name)
    {
        this.value = name;
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

    @Override
    @XmlAttribute(name = "id")
    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

       
    @Override
    public String toString()
    {
        StringBuilder info = new StringBuilder();
        info.append("tag[value='").append(getName()).append("',")
            .append("createdBy='").append(getCreatedBy())
            .append("', id='").append(getId()).append("']");       

        return info.toString();
    }
}
