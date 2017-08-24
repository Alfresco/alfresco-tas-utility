package org.alfresco.utility.data.auth;

import org.alfresco.utility.TasProperties;
import org.alfresco.utility.exception.TestStepException;
import org.alfresco.utility.model.GroupModel;
import org.alfresco.utility.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import junit.framework.Assert;

import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import static org.alfresco.utility.report.log.Step.STEP;

@Service
@Scope(value = "prototype")
public class DataOpenLDAP
{
    enum ObjectType
    {
        user("inetOrgPerson"), group("posixGroup");

        private final String objectType;

        ObjectType(String objectType)
        {
            this.objectType = objectType;
        }

        @Override
        public String toString()
        {
            return objectType;
        }
    }

    @Autowired
    private TasProperties tasProperties;

    private final static String USER_SEARCH_BASE = "cn=%s,ou=users,dc=ldap,dc=dev,dc=alfresco,dc=me";

    private final static String GROUP_SEARCH_BASE = "cn=%s,ou=groups,dc=ldap,dc=dev,dc=alfresco,dc=me";

    private final static String SUBGROUP_SEARCH_BASE = "cn=%s, cn=%s,ou=groups,dc=ldap,dc=dev,dc=alfresco,dc=me";

    private DirContext context;

    public DataOpenLDAP.Builder perform() throws NamingException
    {
        return new DataOpenLDAP.Builder();
    }

    public class Builder implements UserManageable, GroupManageable
    {
        public Builder() throws NamingException
        {
            Properties properties = new Properties();
            properties.put(Context.INITIAL_CONTEXT_FACTORY, tasProperties.getAuthContextFactory());
            properties.put(Context.PROVIDER_URL, tasProperties.getOLdapURL());
            properties.put(Context.SECURITY_AUTHENTICATION, tasProperties.getSecurityAuth());
            properties.put(Context.SECURITY_PRINCIPAL, tasProperties.getOLdapSecurityPrincipal());
            properties.put(Context.SECURITY_CREDENTIALS, tasProperties.getOLdapSecurityCredentials());
            context = new InitialDirContext(properties);
        }

        @Override
        public Builder createUser(UserModel user) throws NamingException
        {
            STEP(String.format("[OpenLDAP] Add user %s", user.getUsername()));
            Attributes attributes = new BasicAttributes();
            Attribute objectClass = new BasicAttribute("objectClass");
            Attribute sn = new BasicAttribute("sn");
            Attribute uid = new BasicAttribute("uid");
            Attribute userPassword = new BasicAttribute("userPassword");

            objectClass.add(DataOpenLDAP.ObjectType.user.toString());
            sn.add(user.getLastName());
            uid.add(user.getUsername());
            userPassword.add(user.getPassword());

            attributes.put(objectClass);
            attributes.put(sn);
            attributes.put(uid);
            attributes.put(userPassword);

            context.createSubcontext(String.format(USER_SEARCH_BASE, user.getUsername()), attributes);
            return this;
        }

        @Override
        public Builder deleteUser(UserModel user) throws NamingException
        {
            STEP(String.format("[OpenLDAP] Delete user %s", user.getUsername()));
            context.destroySubcontext(String.format(USER_SEARCH_BASE, user.getUsername()));
            return this;
        }

        @Override
        public Builder updateUser(UserModel user, HashMap<String, String> attributes) throws NamingException
        {
            STEP(String.format("[OpenLDAP] Update user %s", user.getUsername()));
            ModificationItem[] items = new ModificationItem[attributes.size()];
            int i = 0;
            for (Map.Entry<String, String> entry : attributes.entrySet())
            {
                Attribute attribute = new BasicAttribute(entry.getKey(), entry.getValue());
                items[i] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attribute);
                i++;
            }

            context.modifyAttributes(String.format(USER_SEARCH_BASE, user.getUsername()), items);
            return this;
        }

        @Override
        public Builder createGroup(GroupModel group) throws NamingException
        {
            STEP(String.format("[OpenLDAP] Create group %s", group.getDisplayName()));
            Random random = new Random();
            int gidNumberValue = random.nextInt(10000);

            Attributes attributes = new BasicAttributes();
            Attribute objectClass = new BasicAttribute("objectClass");
            Attribute gidNumber = new BasicAttribute("gidNumber");
            Attribute cn = new BasicAttribute("cn");

            objectClass.add(ObjectType.group.toString());
            gidNumber.add(Integer.toString(gidNumberValue));
            cn.add(group.getDisplayName());

            attributes.put(objectClass);
            attributes.put(gidNumber);
            attributes.put(cn);

            context.createSubcontext(String.format(GROUP_SEARCH_BASE, group.getDisplayName()), attributes);

            return this;
        }

        public Builder createSubGroup(GroupModel subGroup, GroupModel mainGroup) throws NamingException
        {
            STEP(String.format("[OpenLDAP] Create group %s", subGroup.getDisplayName()));
            Random random = new Random();
            int gidNumberValue = random.nextInt(10000);

            Attributes attributes = new BasicAttributes();
            Attribute objectClass = new BasicAttribute("objectClass");
            Attribute gidNumber = new BasicAttribute("gidNumber");
            Attribute cn = new BasicAttribute("cn");
            Attribute cnGroup = new BasicAttribute("cn");

            objectClass.add(ObjectType.group.toString());
            gidNumber.add(Integer.toString(gidNumberValue));
            cn.add(subGroup.getDisplayName());
            cnGroup.add(mainGroup.getDisplayName());

            attributes.put(objectClass);
            attributes.put(gidNumber);
            attributes.put(cn);
            attributes.put(cnGroup);

            context.createSubcontext(String.format(SUBGROUP_SEARCH_BASE, subGroup.getDisplayName(), mainGroup.getDisplayName()), attributes);

            return this;
        }

        @Override
        public GroupManageable deleteGroup(GroupModel group) throws NamingException
        {
            STEP(String.format("[OpenLDAP] Delete group %s", group.getDisplayName()));
            context.destroySubcontext(String.format(GROUP_SEARCH_BASE, group.getDisplayName()));
            return this;
        }

        public Builder deleteSubgroup(GroupModel subgroup, GroupModel group) throws NamingException
        {
            STEP(String.format("[OpenLDAP] Delete subgroup %s from group %s", subgroup.getDisplayName(), group.getDisplayName()));
            context.destroySubcontext(String.format(SUBGROUP_SEARCH_BASE, subgroup.getDisplayName(), group.getDisplayName()));
            return this;
        }

        @Override
        public GroupManageable addUserToGroup(UserModel user, GroupModel group) throws NamingException
        {
            STEP(String.format("[OpenLDAP] Add user %s to group %s", user.getUsername(), group.getDisplayName()));
            Attribute memberAttribute = new BasicAttribute("memberUID", String.format(USER_SEARCH_BASE, user.getUsername()));
            ModificationItem member[] = new ModificationItem[1];
            member[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, memberAttribute);
            context.modifyAttributes(String.format(GROUP_SEARCH_BASE, group.getDisplayName()), member);
            return this;
        }

        public Builder addGroupAsMemberOfAnotherGroup(GroupModel childGroup, GroupModel group) throws NamingException
        {
            STEP(String.format("[OpenLDAP] Add group %s as member of group %s", childGroup.getDisplayName(), group.getDisplayName()));
            Attribute memberAttribute = new BasicAttribute("memberUID", String.format(GROUP_SEARCH_BASE, childGroup.getDisplayName()));
            ModificationItem member[] = new ModificationItem[1];
            member[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, memberAttribute);
            context.modifyAttributes(String.format(GROUP_SEARCH_BASE, group.getDisplayName()), member);
            return this;
        }

        @Override
        public GroupManageable removeUserFromGroup(UserModel user, GroupModel group) throws NamingException
        {
            STEP(String.format("[OpenLDAP] Remove user %s from group %s", user.getUsername(), group.getDisplayName()));
            Attribute memberAttribute = new BasicAttribute("memberUid", String.format(USER_SEARCH_BASE, user.getUsername()));
            ModificationItem member[] = new ModificationItem[1];
            member[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, memberAttribute);
            context.modifyAttributes(String.format(GROUP_SEARCH_BASE, group.getDisplayName()), member);
            return this;
        }

        public SearchResult searchForObjectClass(String name, ObjectType typeOfClass, String base) throws NamingException
        {
            NamingEnumeration<SearchResult> results = null;
            String searchFilter = String.format("(objectClass=%s)", typeOfClass.toString());
            SearchControls searchControls = new SearchControls();
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            try
            {
                results = context.search(String.format(base, name), searchFilter, searchControls);
            }
            catch (NameNotFoundException e)
            {
                return null;
            }
            if (results.hasMoreElements())
                return (SearchResult) results.nextElement();
            return null;
        }

        private SearchResult searchGeneratedData(String partialName, ObjectType typeOfClass, String base) throws NamingException
        {
            NamingEnumeration<SearchResult> results = null;
            String searchFilter = String.format("(objectClass=%s)", typeOfClass.toString());
            SearchControls searchControls = new SearchControls();
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            try
            {
                results = context.search(base.replace("cn=%s,", ""), searchFilter, searchControls);
            }
            catch (NameNotFoundException e)
            {
                return null;
            }
            while (results.hasMoreElements())
            {
                SearchResult rez = results.nextElement();
                if (rez.getNameInNamespace().contains(partialName))
                {
                    return rez;
                }
            }
            return null;
        }

        public Builder deleteBulkUsers() throws NamingException
        {
            STEP(String.format("[OpenLDAP] Delete all users which start with 'user-'"));
            SearchResult rez = searchGeneratedData("cn=user-", ObjectType.user, USER_SEARCH_BASE);
            while (rez != null)
            {
                context.destroySubcontext(rez.getNameInNamespace());
                rez = searchGeneratedData("cn=user-", ObjectType.user, USER_SEARCH_BASE);
            }
            return this;
        }

        public Builder deleteBulkGroups() throws NamingException
        {
            STEP(String.format("[OpenLDAP] Delete all groups which start with 'group-'"));
            SearchResult rez = searchGeneratedData("cn=group-", ObjectType.group, GROUP_SEARCH_BASE);
            while (rez != null)
            {
                context.destroySubcontext(rez.getNameInNamespace());
                rez = searchGeneratedData("cn=group-", ObjectType.group, GROUP_SEARCH_BASE);
            }
            return this;
        }

        @Override
        public UserManageable assertUserExists(UserModel user) throws NamingException
        {
            STEP(String.format("[OpenLDAP] Assert user %s exists", user.getUsername()));
            Assert.assertNotNull(searchForObjectClass(user.getUsername(), ObjectType.user, USER_SEARCH_BASE));
            return this;
        }

        @Override
        public UserManageable assertUserDoesNotExist(UserModel user) throws NamingException, TestStepException
        {
            STEP(String.format("[OpenLDAP] Assert user %s does not exist", user.getUsername()));
            Assert.assertNull(searchForObjectClass(user.getUsername(), ObjectType.user, USER_SEARCH_BASE));
            return this;
        }

        @Override
        public GroupManageable assertGroupExists(GroupModel group) throws NamingException
        {
            STEP(String.format("[OpenLDAP] Assert group %s exists", group.getDisplayName()));
            Assert.assertNotNull(searchForObjectClass(group.getDisplayName(), ObjectType.group, GROUP_SEARCH_BASE));
            return this;
        }

        public Builder assertSubgroupExists(GroupModel subgroup, GroupModel group) throws NamingException
        {
            STEP(String.format("[OpenLDAP] Assert subgroup %s from group %s exists", subgroup.getDisplayName(), group.getDisplayName()));
            Assert.assertNotNull(searchForObjectClass(subgroup.getDisplayName(), ObjectType.group,
                    String.format("%s,%s", "cn=%s", String.format(GROUP_SEARCH_BASE, group.getDisplayName()))));
            return this;
        }

        @Override
        public GroupManageable assertGroupDoesNotExist(GroupModel group) throws NamingException
        {
            STEP(String.format("[OpenLDAP] Assert group %s does not exist", group.getDisplayName()));
            Assert.assertNull(searchForObjectClass(group.getDisplayName(), ObjectType.group, GROUP_SEARCH_BASE));
            return this;
        }

        @Override
        public GroupManageable assertUserIsMemberOfGroup(UserModel user, GroupModel group) throws NamingException
        {
            STEP(String.format("[OpenLDAP] Assert user %s is member of group %s", user.getUsername(), group.getDisplayName()));
            Attributes membership = context.getAttributes(String.format(GROUP_SEARCH_BASE, group.getDisplayName()), new String[] { "memberUid" });
            Assert.assertTrue(membership.toString().contains(String.format(USER_SEARCH_BASE, user.getUsername())));
            return this;
        }

        public Builder assertGroupIsMemberOfGroup(GroupModel childGroup, GroupModel group) throws NamingException
        {
            STEP(String.format("[OpenLDAP] Assert group %s is member of group %s", childGroup.getDisplayName(), group.getDisplayName()));
            Attributes membership = context.getAttributes(String.format(GROUP_SEARCH_BASE, group.getDisplayName()), new String[] { "memberUid" });
            Assert.assertTrue(membership.toString().contains(String.format(GROUP_SEARCH_BASE, childGroup.getDisplayName())));
            return this;
        }

        @Override
        public GroupManageable assertUserIsNotMemberOfGroup(UserModel user, GroupModel group) throws NamingException
        {
            STEP(String.format("[OpenLDAP] Assert user %s is not member of group %s", user.getUsername(), group.getDisplayName()));
            Attributes membership = context.getAttributes(String.format(GROUP_SEARCH_BASE, group.getDisplayName()), new String[] { "member" });
            Assert.assertFalse(membership.toString().contains(String.format(USER_SEARCH_BASE, user.getUsername())));
            return this;
        }
    }

    public String getUserId(UserModel user)
    {
        return String.format(USER_SEARCH_BASE, user.getUsername());
    }
}
