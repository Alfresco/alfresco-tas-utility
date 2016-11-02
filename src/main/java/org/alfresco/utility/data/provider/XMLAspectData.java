package org.alfresco.utility.data.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "aspect")
public class XMLAspectData
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

    public Map<String, Object> getPropertiesAsHashMap()
    {
        Map<String, Object> allProps = new HashMap<String, Object>();
        for (XMLPropertyData p : properties)
        {
            try
            {
                int value = Integer.parseInt(p.getValue());
                allProps.put(p.getName(), value);
            }
            catch (NumberFormatException e)
            {
                allProps.put(p.getName(), p.getValue());
            }

        }
        return allProps;
    }
    
    public boolean hasProperties()
    {
        return !this.getProperties().isEmpty();
    }

    @Override
    public String toString()
    {
        StringBuilder info = new StringBuilder();
        info.append("aspect[name='").append(getName()).append("', properties=[");
        for (XMLPropertyData p : properties)
        {
            info.append("{").append(p.getName()).append("=").append(p.getValue()).append("} ");
        }
        info.append("]");
        return info.toString();
    }

}
