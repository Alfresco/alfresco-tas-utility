package org.alfresco.utility.model;

import javax.xml.bind.annotation.XmlAttribute;

import org.alfresco.utility.constants.UserRole;
import org.alfresco.utility.data.RandomData;
import org.alfresco.utility.data.TestData;
import org.apache.commons.lang3.RandomStringUtils;

public class UserModel extends TestModel
{
    private String username;
    private String password;
    private String domain;
    private String firstName;
    private String lastName;

    /*
     * the role of the user;
     */
    private UserRole userRole;

    public UserModel()
    {
    }

    public UserModel(String username, String password)
    {
        this.setUsername(username);
        this.setPassword(password);
    }

    public UserModel(String username, String password, String domain)
    {
        this.setUsername(username);
        this.setPassword(password);
        this.setDomain(domain);
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
        if (getDomain() == null)
            return username;
        else
        {
            return getEmailAddress();
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

    public String getEmailAddress()
    {
        return String.format("%s@%s", username, getDomain());
    }

    public static UserModel getRandomTenantUser()
    {
        UserModel tenantUser = new UserModel(RandomData.getRandomName("tenant"), TestData.PASSWORD);
        tenantUser.setDomain(getRandomTenant("tenant"));
        return tenantUser;
    }

    public static UserModel getAdminTenantUser()
    { 
        return getAdminUserForTenant(getRandomTenant("tenant"));
    }
    
    public static UserModel getAdminUserForTenant(String tenantName)
    {
        UserModel tenantAdmin = new UserModel("admin", TestData.PASSWORD);
        tenantAdmin.setDomain(tenantName);
        return tenantAdmin;
    }

    public static UserModel getRandomUserModel()
    {
        String randomUsername = RandomData.getRandomName("user");
        UserModel user = new UserModel(randomUsername, TestData.PASSWORD);
        user.setFirstName(String.format("%s-FN", randomUsername));
        user.setLastName(String.format("%s-LN", randomUsername));
        return user;
    }

    private static String getRandomTenant(String prefix)
    {
        return String.format("%s%s", prefix, RandomStringUtils.randomAlphabetic(6));
    }

    public UserRole getUserRole()
    {
        return userRole;
    }

    public void setUserRole(UserRole userRole)
    {
        this.userRole = userRole;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

}
