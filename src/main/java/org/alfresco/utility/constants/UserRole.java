package org.alfresco.utility.constants;

import java.util.Random;

/**
 * Alfresco User Roles
 */
public enum UserRole
{
    SiteManager("{http://www.alfresco.org/model/site/1.0}site.SiteManager"),
    SiteContributor("{http://www.alfresco.org/model/site/1.0}site.SiteContributor"),
    SiteCollaborator("{http://www.alfresco.org/model/site/1.0}site.SiteCollaborator"),
    SiteConsumer("{http://www.alfresco.org/model/site/1.0}site.SiteConsumer");
    
    private final String roleId;
    private UserRole(String roleId)
    {
        this.roleId = roleId;
    }

    public String getRoleId()
    {
        return this.roleId;
    }

    private static Random random = new Random();

    public static UserRole randomRole()
    {
        UserRole[] roles = UserRole.values();
        int idx = random.nextInt(roles.length);
        return roles[idx];
    }
}
