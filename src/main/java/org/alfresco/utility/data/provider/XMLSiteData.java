package org.alfresco.utility.data.provider;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.alfresco.utility.model.SiteModel;

/**
 * <site name="site1" createdBy="admin">
 *
 */
@XmlType(name = "site")
public class XMLSiteData
{
    private String name;
    private String createdBy;
    
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
    
    @Override
    public String toString()
    {
        StringBuilder info = new StringBuilder();
        info.append("site[name='")
            .append(getName()).append("',")
            .append("createdBy='")
            .append(getCreatedBy()).append("']");
                
        return info.toString(); 
    }
    
    public SiteModel toSiteModel()
    {
        return new SiteModel(getName());
    }
}
