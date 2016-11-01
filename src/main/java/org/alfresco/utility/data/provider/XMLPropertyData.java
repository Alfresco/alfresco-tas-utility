package org.alfresco.utility.data.provider;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.alfresco.utility.model.TestModel;

/**
 * <property name="tas:TextPropertyC" value = "abcd"/>
 */
@XmlType(name = "property")
public class XMLPropertyData implements XMLDataItem
{
    private String name;
    private String value;
    private String id;

    @XmlAttribute(name = "name")
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @XmlAttribute(name = "value")
    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
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
        info.append("property[name='").append(getName()).append("',")
            .append("value='").append(getValue())
            .append("', id='").append(getId()).append("']");       

        return info.toString();
    }

}
