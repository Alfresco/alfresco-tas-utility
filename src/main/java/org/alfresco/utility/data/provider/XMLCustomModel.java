package org.alfresco.utility.data.provider;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

import org.alfresco.utility.data.CustomObjectTypeProperties;
import org.alfresco.utility.model.TestModel;

@XmlType(name = "custom-model")
public class XMLCustomModel implements XMLDataItem 
{
    private String name;
    private String id;
    
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

    @Override
    public TestModel getModel()
    {        
        return new TestModel(){
            
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
        info.append("custom-model[name='").append(getName()).append("',")
            .append("id='").append(getId()).append("']");

        return info.toString();
    }
}
