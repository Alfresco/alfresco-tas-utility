package org.alfresco.utility.data.auth;

import org.alfresco.utility.TasProperties;
import org.alfresco.utility.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.testng.Assert;

import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.alfresco.utility.report.log.Step.STEP;

/**
 * Created by Claudia Agache on 6/14/2017.
 */
@Service
@Scope(value = "prototype")
public class DataOracleDirectoryServer
{
    @Autowired
    private TasProperties tasProperties;

    private final static String USER_SEARCH_BASE = "cn=%s,ou=People,dc=alfness,dc=com";

    private DirContext context;

    public DataOracleDirectoryServer.Builder perform() throws NamingException
    {
        return new DataOracleDirectoryServer.Builder();
    }

    public class Builder implements UserManageable
    {
        public Builder() throws NamingException
        {
            Properties properties = new Properties();
            properties.put(Context.INITIAL_CONTEXT_FACTORY, tasProperties.getAuthContextFactory());
            properties.put(Context.PROVIDER_URL, tasProperties.getOracleURL());
            properties.put(Context.SECURITY_AUTHENTICATION, tasProperties.getSecurityAuth());
            properties.put(Context.SECURITY_PRINCIPAL, tasProperties.getOracleSecurityPrincipal());
            properties.put(Context.SECURITY_CREDENTIALS, tasProperties.getOracleSecurityCredentials());
            context = new InitialDirContext(properties);
        }

        @Override
        public UserManageable createUser(UserModel user) throws NamingException
        {
            STEP(String.format("[OracleDirServer] Add user %s", user.getUsername()));
            Attributes attributes = new BasicAttributes();
            Attribute objectClass = new BasicAttribute("objectClass");
            Attribute sn = new BasicAttribute("sn");
            Attribute userPassword = new BasicAttribute("userPassword");

            objectClass.add("inetOrgPerson");
            sn.add(user.getLastName());
            userPassword.add(user.getPassword());

            attributes.put(objectClass);
            attributes.put(sn);
            attributes.put(userPassword);

            context.createSubcontext(String.format(USER_SEARCH_BASE, user.getUsername()), attributes);
            return this;
        }

        @Override
        public UserManageable deleteUser(UserModel user) throws NamingException
        {
            STEP(String.format("[OracleDirServer] Delete user %s", user.getUsername()));
            context.destroySubcontext(String.format(USER_SEARCH_BASE, user.getUsername()));
            return this;
        }

        @Override
        public UserManageable updateUser(UserModel user, HashMap<String, String> attributes) throws NamingException
        {
            STEP(String.format("[OracleDirServer] Update user %s", user.getUsername()));
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

        public SearchResult searchForObjectClass(String name, String typeOfClass, String base) throws NamingException
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

        @Override
        public UserManageable assertUserExists(UserModel user) throws NamingException
        {
            STEP(String.format("[OracleDirServer] Assert user %s exists", user.getUsername()));
            Assert.assertNotNull(searchForObjectClass(user.getUsername(), "inetOrgPerson", USER_SEARCH_BASE));
            return this;
        }

        @Override
        public UserManageable assertUserDoesNotExist(UserModel user) throws NamingException
        {
            STEP(String.format("[OracleDirServer] Assert user %s does not exist", user.getUsername()));
            Assert.assertNull(searchForObjectClass(user.getUsername(), "inetOrgPerson", USER_SEARCH_BASE));
            return this;
        }
    }
}
