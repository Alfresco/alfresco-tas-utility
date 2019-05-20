package org.alfresco.utility.data.auth;

import org.alfresco.utility.TasProperties;
import org.alfresco.utility.exception.TestStepException;
import org.alfresco.utility.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.testng.Assert;

import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.Context;
import javax.naming.directory.*;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.alfresco.utility.report.log.Step.STEP;

@Service
@Scope(value = "prototype")
public class DataKerberos
{
    @Autowired
    private TasProperties tasProperties;

    public enum UserAccountStatus
    {
        NORMAL_ACCOUNT(0x0200),
        PASSWD_NOTREQD(0x0020),
        DONT_REQ_PREAUTH(0x400000),
        TRUSTED_TO_AUTH_FOR_DELEGATION(0x1000000),
        TRUSTED_FOR_DELEGATION(0x80000),
        DISABLE_ACCOUNT(0x0002),
        DONT_EXPIRE_PASSWD(0x10000);

        private final int value;

        UserAccountStatus(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }
    }

    public enum UserAccountControlValue
    {
        enabled("21037568"), disabled("21037570");

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

    public Builder perform() throws NamingException
    {
        return new Builder();
    }

    private final static String USER_SEARCH_BASE = "CN=%s,CN=Users,DC=alfresconess,DC=com";

    private DirContext context;

    public class Builder implements UserManageable
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
        public Builder createUser(UserModel user) throws NamingException, UnsupportedEncodingException
        {
            STEP(String.format("[Kerberos] Add user %s", user.getUsername()));
            Attributes attributes = new BasicAttributes();
            Attribute objectClass = new BasicAttribute("objectClass", DataLDAP.ObjectType.user.toString());
            Attribute sn = new BasicAttribute("sn", user.getLastName());
            Attribute samAccountName = new BasicAttribute("samAccountName", user.getUsername());
            Attribute userAccountControl = new BasicAttribute("userAccountControl");

            userAccountControl.add(Integer.toString(UserAccountStatus.NORMAL_ACCOUNT.getValue() + UserAccountStatus.PASSWD_NOTREQD.getValue()
                    + UserAccountStatus.DONT_EXPIRE_PASSWD.getValue() + UserAccountStatus.TRUSTED_TO_AUTH_FOR_DELEGATION.getValue()
                    + UserAccountStatus.DONT_REQ_PREAUTH.getValue()));
            attributes.put(objectClass);
            attributes.put(sn);
            attributes.put(samAccountName);
            attributes.put(userAccountControl);

            context.createSubcontext(String.format(USER_SEARCH_BASE, user.getUsername()), attributes);

            String newQuotedPassword = String.format("\"%s\"", user.getPassword());
            byte[] newUnicodePassword = newQuotedPassword.getBytes("UTF-16LE");

            ModificationItem[] mods = new ModificationItem[2];
            mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("unicodePwd", newUnicodePassword));
            mods[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                    new BasicAttribute("userAccountControl",
                            Integer.toString(UserAccountStatus.NORMAL_ACCOUNT.getValue() + UserAccountStatus.DONT_EXPIRE_PASSWD.getValue()
                                    + UserAccountStatus.TRUSTED_TO_AUTH_FOR_DELEGATION.getValue() + UserAccountStatus.DONT_REQ_PREAUTH.getValue())));
            context.modifyAttributes(String.format(USER_SEARCH_BASE, user.getUsername()), mods);
            return this;
        }

        public SearchResult searchForObjectClass(String name, DataLDAP.ObjectType typeOfClass) throws NamingException
        {
            NamingEnumeration<SearchResult> results = null;
            String searchFilter = String.format("(objectClass=%s)", typeOfClass.toString());
            SearchControls searchControls = new SearchControls();
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            try
            {
                results = context.search(String.format(USER_SEARCH_BASE, name), searchFilter, searchControls);
            }
            catch (NameNotFoundException e)
            {
                return null;
            }
            if (results.hasMoreElements())
                return (SearchResult) results.nextElement();
            return null;
        }

        @Override
        public UserManageable deleteUser(UserModel user) throws NamingException
        {
            STEP(String.format("[Kerberos] Delete user %s", user.getUsername()));
            context.destroySubcontext(String.format(USER_SEARCH_BASE, user.getUsername()));
            return this;
        }

        @Override
        public UserManageable updateUser(UserModel user, HashMap<String, String> attributes) throws NamingException, UnsupportedEncodingException
        {
            STEP(String.format("[Kerberos] Update user %s", user.getUsername()));
            ModificationItem[] items = new ModificationItem[attributes.size()];
            int i = 0;
            for (Map.Entry<String, String> entry : attributes.entrySet())
            {
                Attribute attribute = new BasicAttribute(entry.getKey());
                if(entry.getKey().equals("unicodePwd"))
                {
                    String newQuotedPassword = String.format("\"%s\"", entry.getValue());
                    byte[] newUnicodePassword = newQuotedPassword.getBytes("UTF-16LE");
                    attribute.add(newUnicodePassword);
                }
                else
                {
                    attribute.add(entry.getValue());
                }
                items[i] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attribute);
                i++;
            }
            context.modifyAttributes(String.format(USER_SEARCH_BASE, user.getUsername()), items);
            return this;
        }

        public Builder disableUser(UserModel user) throws NamingException
        {
            STEP(String.format("[Kerberos] Disable user %s", user.getUsername()));
            Attribute memberAttribute = new BasicAttribute("userAccountControl", Integer.toString(
                    UserAccountStatus.DISABLE_ACCOUNT.getValue() + UserAccountStatus.NORMAL_ACCOUNT.getValue() + UserAccountStatus.DONT_EXPIRE_PASSWD.getValue()
                            + UserAccountStatus.TRUSTED_TO_AUTH_FOR_DELEGATION.getValue() + UserAccountStatus.DONT_REQ_PREAUTH.getValue()));
            ModificationItem modification[] = new ModificationItem[1];
            modification[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, memberAttribute);
            context.modifyAttributes(String.format(USER_SEARCH_BASE, user.getUsername()), modification);
            return this;
        }

        public Builder enableUser(UserModel user) throws NamingException
        {
            STEP(String.format("[Kerberos] Enable user %s", user.getUsername()));
            Attribute memberAttribute = new BasicAttribute("userAccountControl",
                    Integer.toString(UserAccountStatus.NORMAL_ACCOUNT.getValue() + UserAccountStatus.DONT_EXPIRE_PASSWD.getValue()
                            + UserAccountStatus.TRUSTED_TO_AUTH_FOR_DELEGATION.getValue() + UserAccountStatus.DONT_REQ_PREAUTH.getValue()));
            ModificationItem modification[] = new ModificationItem[1];
            modification[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, memberAttribute);
            context.modifyAttributes(String.format(USER_SEARCH_BASE, user.getUsername()), modification);
            return this;
        }

        @Override
        public UserManageable assertUserExists(UserModel user) throws NamingException
        {
            STEP(String.format("[Kerberos] Assert user %s exists", user.getUsername()));
            Assert.assertNotNull(searchForObjectClass(user.getUsername(), DataLDAP.ObjectType.user));
            return this;
        }

        @Override
        public UserManageable assertUserDoesNotExist(UserModel user) throws NamingException, TestStepException
        {
            STEP(String.format("[Kerberos] Assert user %s does not exist", user.getUsername()));
            Assert.assertNull(searchForObjectClass(user.getUsername(), DataLDAP.ObjectType.user));
            return this;
        }

        public Builder assertUserIsDisabled(UserModel user, UserAccountControlValue userAccountControlValue) throws NamingException
        {
            Attributes accountStatus = context.getAttributes(String.format(USER_SEARCH_BASE, user.getUsername()), new String[] { "userAccountControl" });
            Assert.assertTrue(accountStatus.toString().contains(userAccountControlValue.toString()),
                    String.format("User account control value expected %s but found %s", userAccountControlValue.toString(), accountStatus.toString()));
            return this;
        }

        public Builder assertUserIsEnabled(UserModel user, UserAccountControlValue userAccountControlValue) throws NamingException
        {
            Attributes accountStatus = context.getAttributes(String.format(USER_SEARCH_BASE, user.getUsername()), new String[] { "userAccountControl" });
            Assert.assertTrue(accountStatus.toString().contains(userAccountControlValue.toString()),
                    String.format("User account value expected %s but found %s ", userAccountControlValue.toString(), accountStatus.toString()));
            return this;
        }
    }
}
