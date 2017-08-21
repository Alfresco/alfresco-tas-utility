package org.alfresco.utility.data.auth;

import org.alfresco.utility.TasProperties;
import org.alfresco.utility.exception.TestStepException;
import org.alfresco.utility.model.GroupModel;
import org.alfresco.utility.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.testng.Assert;

import javax.naming.*;
import javax.naming.directory.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.alfresco.utility.report.log.Step.STEP;

@Service
@Scope(value = "prototype")
public class DataLDAP
{
    enum ObjectType
    {
        user("user"), group("group");

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
    public enum UserAccountControlValue
    {
        enabled("512"), disabled("514"), enabledPasswordNotRequired("544"), disabledPasswordNotRequired("546");

        private final String userAccountControlValue;

        UserAccountControlValue(String userAccountControlValue)
        {
            this.userAccountControlValue = userAccountControlValue;
        }

        @Override
        public String toString()
        {
            return userAccountControlValue;
        }
    }

    enum UserAccountStatus
    {
        ACCOUNTDISABLE(0x0002), NORMAL_ACCOUNT(0x0200), PASSWD_NOTREQD(0x0020), PASSWORD_EXPIRED(0x800000);

        private final int value;

        private UserAccountStatus(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }
    }

    @Autowired
    private TasProperties tasProperties;

    private final static String USER_GROUP_SEARCH_BASE = "CN=%s,CN=Users,DC=alfness,DC=com";

    private DirContext context;

    public DataLDAP.Builder perform() throws NamingException
    {
        return new DataLDAP.Builder();
    }

    public class Builder implements UserManageable, GroupManageable
    {
        public Builder() throws NamingException
        {
            Properties properties = new Properties();
            properties.put(Context.INITIAL_CONTEXT_FACTORY, tasProperties.getAuthContextFactory());
            properties.put(Context.PROVIDER_URL, tasProperties.getLdapURL());
            properties.put(Context.SECURITY_AUTHENTICATION, tasProperties.getSecurityAuth());
            properties.put(Context.SECURITY_PRINCIPAL, tasProperties.getLdapSecurityPrincipal());
            properties.put(Context.SECURITY_CREDENTIALS, tasProperties.getLdapSecurityCredentials());
            context = new InitialDirContext(properties);
        }

        @Override
        public Builder createUser(UserModel user) throws NamingException
        {
            STEP(String.format("[LDAP-AD] Add user %s", user.getUsername()));
            Attributes attributes = new BasicAttributes();
            Attribute objectClass = new BasicAttribute("objectClass");
            Attribute sn = new BasicAttribute("sn");
            Attribute userPassword = new BasicAttribute("userPassword");

            objectClass.add(ObjectType.user.toString());
            sn.add(user.getLastName());
            userPassword.add(user.getPassword());

            attributes.put(objectClass);
            attributes.put(sn);
            attributes.put(userPassword);

            context.createSubcontext(String.format(USER_GROUP_SEARCH_BASE, user.getUsername()), attributes);
            return this;
        }

        @Override
        public Builder deleteUser(UserModel user) throws NamingException
        {
            STEP(String.format("[LDAP-AD] Delete user %s", user.getUsername()));
            context.destroySubcontext(String.format(USER_GROUP_SEARCH_BASE, user.getUsername()));
            return this;
        }

        @Override
        public Builder updateUser(UserModel user, HashMap<String, String> attributes) throws NamingException
        {
            STEP(String.format("[LDAP-AD] Update user %s", user.getUsername()));
            ModificationItem[] items = new ModificationItem[attributes.size()];
            int i = 0;
            for (Map.Entry<String, String> entry : attributes.entrySet())
            {
                Attribute attribute = new BasicAttribute(entry.getKey(), entry.getValue());
                items[i] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attribute);
                i++;
            }

            context.modifyAttributes(String.format(USER_GROUP_SEARCH_BASE, user.getUsername()), items);
            return this;
        }

        @Override
        public Builder createGroup(GroupModel group) throws NamingException
        {
            STEP(String.format("[LDAP-AD] Create group %s", group.getDisplayName()));
            Attributes attributes = new BasicAttributes();

            Attribute objectClass = new BasicAttribute("objectClass");
            Attribute samAccountName = new BasicAttribute("samAccountName");
            Attribute name = new BasicAttribute("name");

            objectClass.add(ObjectType.group.toString());
            samAccountName.add(group.getDisplayName());
            name.add(group.getDisplayName());

            attributes.put(objectClass);
            attributes.put(samAccountName);
            attributes.put(name);

            context.createSubcontext(String.format(USER_GROUP_SEARCH_BASE, group.getDisplayName()), attributes);

            return this;
        }

        @Override
        public Builder deleteGroup(GroupModel group) throws NamingException
        {
            STEP(String.format("[LDAP-AD] Delete group %s", group.getDisplayName()));
            context.destroySubcontext(String.format(USER_GROUP_SEARCH_BASE, group.getDisplayName()));
            return this;
        }

        @Override
        public Builder addUserToGroup(UserModel user, GroupModel group) throws NamingException
        {
            STEP(String.format("[LDAP-AD] Add user % to group %s", user.getUsername(), group.getDisplayName()));
            Attribute memberAttribute = new BasicAttribute("member", String.format(USER_GROUP_SEARCH_BASE, user.getUsername()));
            ModificationItem member[] = new ModificationItem[1];
            member[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, memberAttribute);
            context.modifyAttributes(String.format(USER_GROUP_SEARCH_BASE, group.getDisplayName()), member);
            return this;
        }

        @Override
        public Builder removeUserFromGroup(UserModel user, GroupModel group) throws NamingException
        {
            STEP(String.format("[LDAP-AD] Remove user % from group %s", user.getUsername(), group.getDisplayName()));
            Attribute memberAttribute = new BasicAttribute("member", String.format(USER_GROUP_SEARCH_BASE, user.getUsername()));
            ModificationItem member[] = new ModificationItem[1];
            member[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, memberAttribute);
            context.modifyAttributes(String.format(USER_GROUP_SEARCH_BASE, group.getDisplayName()), member);
            return this;
        }

        public Builder disableUser(UserModel user) throws NamingException
        {
            Attribute memberAttribute = new BasicAttribute("userAccountControl", Integer.toString(
                    UserAccountStatus.ACCOUNTDISABLE.getValue() + UserAccountStatus.NORMAL_ACCOUNT.getValue() + UserAccountStatus.PASSWD_NOTREQD.getValue()));
            ModificationItem modification[] = new ModificationItem[1];
            modification[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, memberAttribute);
            context.modifyAttributes(String.format(USER_GROUP_SEARCH_BASE, user.getUsername()), modification);
            return this;
        }

        public Builder enableUser(UserModel user) throws NamingException
        {
            Attribute memberAttribute = new BasicAttribute("userAccountControl",
                    Integer.toString(UserAccountStatus.NORMAL_ACCOUNT.getValue() + UserAccountStatus.PASSWD_NOTREQD.getValue()));
            ModificationItem modification[] = new ModificationItem[1];
            modification[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, memberAttribute);
            context.modifyAttributes(String.format(USER_GROUP_SEARCH_BASE, user.getUsername()), modification);
            return this;
        }

        public SearchResult searchForObjectClass(String name, ObjectType typeOfClass) throws NamingException
        {
            NamingEnumeration<SearchResult> results = null;
            String searchFilter = String.format("(objectClass=%s)", typeOfClass.toString());
            SearchControls searchControls = new SearchControls();
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            try
            {
                results = context.search(String.format(USER_GROUP_SEARCH_BASE, name), searchFilter, searchControls);
            }
            catch (NameNotFoundException e)
            {
                return null;
            }
            if (results.hasMoreElements())
                return (SearchResult) results.nextElement();
            return null;
        }

        public Builder createEnabledUserPasswordNotRequired(UserModel user) throws NamingException
        {
            Attributes attributes = new BasicAttributes();
            Attribute objectClass = new BasicAttribute("objectClass");
            Attribute sn = new BasicAttribute("sn");
            Attribute samAccountName = new BasicAttribute("samAccountName");
            Attribute userPassword = new BasicAttribute("userPassword");
            Attribute userAccountControl = new BasicAttribute("userAccountControl");

            objectClass.add(ObjectType.user.toString());
            sn.add(user.getLastName());
            samAccountName.add(user.getUsername());
            userPassword.add(user.getPassword());
            userAccountControl.add(Integer.toString(
                    UserAccountStatus.NORMAL_ACCOUNT.getValue() + UserAccountStatus.PASSWD_NOTREQD.getValue() + UserAccountStatus.PASSWORD_EXPIRED.getValue()));

            attributes.put(objectClass);
            attributes.put(sn);
            attributes.put(samAccountName);
            attributes.put(userPassword);
            attributes.put(userAccountControl);

            context.createSubcontext(String.format(USER_GROUP_SEARCH_BASE, user.getUsername()), attributes);
            return this;
        }

        public Builder createDisabledUser(UserModel user) throws NamingException
        {
            Attributes attributes = new BasicAttributes();
            Attribute objectClass = new BasicAttribute("objectClass");
            Attribute sn = new BasicAttribute("sn");
            Attribute samAccountName = new BasicAttribute("samAccountName");
            Attribute userPassword = new BasicAttribute("userPassword");
            Attribute userAccountControl = new BasicAttribute("userAccountControl");

            objectClass.add(ObjectType.user.toString());
            sn.add(user.getLastName());
            samAccountName.add(user.getUsername());
            userPassword.add(user.getPassword());
            userAccountControl.add(Integer.toString(
                    UserAccountStatus.NORMAL_ACCOUNT.getValue() + UserAccountStatus.PASSWORD_EXPIRED.getValue() + UserAccountStatus.ACCOUNTDISABLE.getValue()));

            attributes.put(objectClass);
            attributes.put(sn);
            attributes.put(samAccountName);
            attributes.put(userAccountControl);

            context.createSubcontext(String.format(USER_GROUP_SEARCH_BASE, user.getUsername()), attributes);
            return this;
        }

        @Override
        public Builder assertUserExists(UserModel user) throws NamingException
        {
            STEP(String.format("[LDAP-AD] Assert user %s exists", user.getUsername()));
            Assert.assertNotNull(searchForObjectClass(user.getUsername(), ObjectType.user));
            return this;
        }

        public UserManageable assertUserDoesNotExist(UserModel user) throws NamingException, TestStepException
        {
            STEP(String.format("[LDAP-AD] Assert user %s does not exist", user.getUsername()));
            Assert.assertNull(searchForObjectClass(user.getUsername(), ObjectType.user));
            return this;
        }

        public GroupManageable assertGroupExists(GroupModel group) throws NamingException
        {
            STEP(String.format("[LDAP-AD] Assert group %s exists", group.getDisplayName()));
            Assert.assertNotNull(searchForObjectClass(group.getDisplayName(), ObjectType.group));
            return this;
        }

        public GroupManageable assertGroupDoesNotExist(GroupModel group) throws NamingException
        {
            STEP(String.format("[LDAP-AD] Assert group %s does not exist", group.getDisplayName()));
            Assert.assertNull(searchForObjectClass(group.getDisplayName(), ObjectType.group));
            return this;
        }

        public Builder assertUserIsDisabled(UserModel user, UserAccountControlValue userAccountControlValue) throws NamingException
        {
            Attributes accountStatus = context.getAttributes(String.format(USER_GROUP_SEARCH_BASE, user.getUsername()), new String[] { "userAccountControl" });
            Assert.assertTrue(accountStatus.toString().contains(userAccountControlValue.toString()),
                    String.format("User account control value expected %s but found %s", userAccountControlValue.toString(), accountStatus.toString()));
            return this;
        }

        public Builder assertUserIsEnabled(UserModel user, UserAccountControlValue userAccountControlValue) throws NamingException
        {
            Attributes accountStatus = context.getAttributes(String.format(USER_GROUP_SEARCH_BASE, user.getUsername()), new String[] { "userAccountControl" });
            Assert.assertTrue(accountStatus.toString().contains(userAccountControlValue.toString()),
                    String.format("User account value expected %s but found %s ", userAccountControlValue.toString(), accountStatus.toString()));
            return this;
        }

        @Override
        public GroupManageable assertUserIsMemberOfGroup(UserModel user, GroupModel group) throws NamingException
        {
            STEP(String.format("[LDAP-AD] Assert user % is member of group %s", user.getUsername(), group.getDisplayName()));
            Attributes membership = context.getAttributes(String.format(USER_GROUP_SEARCH_BASE, group.getDisplayName()), new String[] { "member" });
            Assert.assertTrue(membership.toString().contains(String.format(USER_GROUP_SEARCH_BASE, user.getUsername())),
                    String.format("User %s is not member of group %s", user.getUsername().toString(), group.getDisplayName().toString()));
            return this;
        }

        @Override
        public GroupManageable assertUserIsNotMemberOfGroup(UserModel user, GroupModel group) throws NamingException
        {
            STEP(String.format("[LDAP-AD] Assert user % is not member of group %s", user.getUsername(), group.getDisplayName()));
            Attributes membership = context.getAttributes(String.format(USER_GROUP_SEARCH_BASE, group.getDisplayName()), new String[] { "member" });
            Assert.assertFalse(membership.toString().contains(String.format(USER_GROUP_SEARCH_BASE, user.getUsername())),
                    String.format("User %s is member of group %s", user.getUsername().toString(), group.getDisplayName().toString()));
            return this;
        }
    }
}
