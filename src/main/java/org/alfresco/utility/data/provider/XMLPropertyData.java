package org.alfresco.utility.data.provider;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * <property name="tas:TextPropertyC" value = "abcd"/>
 */
@XmlType(name = "property")
public class XMLPropertyData
{
    private String name;
    private Object value;

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
    public Object getValue()
    {
        return value;
    }

    public void setValue(Object value)
    {
        this.value = value;
    }

}
