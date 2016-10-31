package org.alfresco.utility.data.provider;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

import org.alfresco.utility.data.CustomObjectTypeProperties;

@XmlType(name = "custom-model")
public class XMLCustomModel
{
    private String name;
    private List<XMLPropertyData> properties = new ArrayList<XMLPropertyData>();

    @XmlAttribute(name = "name")
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @XmlElementWrapper
    @XmlElement(name = "property")
    public List<XMLPropertyData> getProperties()
    {
        return properties;
    }

    public void setProperties(List<XMLPropertyData> properties)
    {
        this.properties = properties;
    }

    public CustomObjectTypeProperties getObjectTypeProperties()
    {        
        CustomObjectTypeProperties customProps = new CustomObjectTypeProperties();
        for(XMLPropertyData p : properties)
        {
            customProps.addProperty(p.getName(), p.getValue());
        }
        return customProps;
    }
     
}
