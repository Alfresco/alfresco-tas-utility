package org.alfresco.utility.data;

import java.util.Random;

/**
 * Alfresco User Roles
 */
public enum UserRole
{
    SiteManager, SiteContributor, SiteCollaborator, SiteConsumer;
    private static Random random = new Random();

    public static UserRole randomRole()
    {
        UserRole[] roles = UserRole.values();
        int idx = random.nextInt(roles.length);
        return roles[idx];
    }
}
