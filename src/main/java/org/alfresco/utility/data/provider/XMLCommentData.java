package org.alfresco.utility.data.provider;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * <comment value="comment file " createdBy="user-adduser"/>
 * 
 * @author Cristina Axinte
 *
 */
@XmlType(name = "comment")
public class XMLCommentData
{
    private String value;
    private String createdBy;
    
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
}
