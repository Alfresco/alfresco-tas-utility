package org.alfresco.utility.model;

import javax.xml.bind.annotation.XmlAttribute;

import org.alfresco.utility.data.RandomData;
import org.alfresco.utility.data.TestData;

public class UserModel extends TestModel
{
    private String username;
    private String password;
    private String domain;

    public UserModel()
    {

    }

    public UserModel(String username, String password)
    {
        this.setUsername(username);
        this.setPassword(password);
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    @XmlAttribute(name = "name")
    public String getUsername()
    {
        if (getDomain() != null)
            return username;
        else
        {
            return getTenantUsername();
        }
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getDomain()
    {
        return domain;
    }

    public void setDomain(String domain)
    {
        this.domain = domain;
    }

    public String getTenantUsername()
    {
        return String.format("%s@%s", getUsername(), getDomain());
    }

    public static UserModel getRandomTenandUser()
    {
        UserModel tenant = new UserModel(RandomData.getRandomName("tenant"), TestData.PASSWORD);
        tenant.setDomain("tenant.com");
        return tenant;
    }

    public static UserModel getRandomUserModel()
    {
        UserModel tenant = new UserModel(RandomData.getRandomName("user"), TestData.PASSWORD);
        return tenant;
    }
}
