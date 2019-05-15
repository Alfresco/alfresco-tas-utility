package org.alfresco.utility.data.provider;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.alfresco.utility.model.TestModel;

/**
 * <comment value="comment file " createdBy="user-adduser"/>
 * 
 * @author Cristina Axinte
 *
 */
@XmlType(name = "comment")
public class XMLCommentData implements XMLDataItem
{
    private String value;
    private String createdBy;
    private String id;
    
    @XmlAttribute(name = "value")
    public String getValue()
    {
        return value;
    }
    public void setValue(String value)
    {
        this.value = value;
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
    public TestModel getModel()
    {
        return new TestModel()
        {                 
        };
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
        info.append("comment[value='").append(getValue()).append("',")
            .append("id='").append(getId()).append("']");

        return info.toString();
    }
}
