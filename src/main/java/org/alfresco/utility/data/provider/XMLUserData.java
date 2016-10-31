package org.alfresco.utility.data.provider;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.alfresco.utility.model.UserModel;

@XmlType(name = "user")
public class XMLUserData implements XMLDataItem
{
    private String name;
    private String password;
    private String role;
    private String id;

    @Override
    public UserModel getModel()
    {
        return new UserModel(getName(), getPassword());
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

    @XmlAttribute(name = "password")
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
    
    @XmlAttribute(name = "role")
    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }
  
    @Override
    public String toString()
    {
        StringBuilder info = new StringBuilder();
        info.append("user[name='")
            .append(getName()).append("',")
            .append("password='")
            .append(getPassword()).append("']");

        return info.toString();
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

}
