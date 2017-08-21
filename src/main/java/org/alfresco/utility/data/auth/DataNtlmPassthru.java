package org.alfresco.utility.data.auth;

import java.util.HashMap;
import java.util.Map;

import org.alfresco.utility.TasProperties;
import org.alfresco.utility.Utility;
import org.alfresco.utility.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Created by Claudia Agache on 6/23/2017.
 *
 * https://support.microsoft.com/en-in/help/322684/how-to-use-the-directory-service-command-line-tools-to-manage-active-directory-objects-in-windows-server-2003
 */
@Service
@Scope(value = "prototype")
public class DataNtlmPassthru
{
    @Autowired
    private TasProperties tasProperties;
    private final static String USER_SEARCH_BASE = "CN=%s,CN=Users,DC=alfntlm,DC=com";
    private String command, psCommand;

    public DataNtlmPassthru.Builder perform()
    {
        return new DataNtlmPassthru.Builder();
    }

    public class Builder implements UserManageable
    {
        public Builder()
        {
            psCommand = String.format("psexec \\\\%s -u %s -p %s", tasProperties.getNtlmHost(), tasProperties.getNtlmSecurityPrincipal(), tasProperties.getNtlmSecurityCredentials());
        }

        @Override
        public Builder createUser(UserModel user) throws Exception
        {
            command = String.format("%s dsadd user \"%s\" -samid %s -upn %s@alfntlm.com -fn %s -ln %s -display \"%s %s\" -disabled no -pwd %s -mustchpwd no -memberof \"cn=Remote Desktop Users,cn=Builtin,dc=alfntlm,dc=com\" ",
                    psCommand, String.format(USER_SEARCH_BASE, user.getUsername()), user.getUsername(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getFirstName(), user.getLastName(), user.getPassword());
            Utility.executeOnWin(command);
            return this;
        }

        @Override
        public Builder deleteUser(UserModel user) throws Exception
        {
            command = String.format("%s dsrm -noprompt \"%s\"", psCommand, String.format(USER_SEARCH_BASE, user.getUsername()));
            Utility.executeOnWin(command);
            return this;
        }

        @Override
        public Builder updateUser(UserModel user, HashMap<String, String> attributes) throws Exception
        {
            command = String.format("%s dsmod user \"%s\" ", psCommand, String.format(USER_SEARCH_BASE, user.getUsername()));
            for (Map.Entry<String, String> entry : attributes.entrySet())
            {
                command += String.format("-%s \"%s\" ", entry.getKey(), entry.getValue());
            }
            Utility.executeOnWin(command);
            return this;
        }

        public Builder disableUser(UserModel user) throws Exception
        {
            command = String.format("%s dsmod user \"%s\" -disabled yes", psCommand, String.format(USER_SEARCH_BASE, user.getUsername()));
            Utility.executeOnWin(command);
            return this;
        }

        public Builder enableUser(UserModel user) throws Exception
        {
            command = String.format("%s dsmod user \"%s\" -disabled no", psCommand, String.format(USER_SEARCH_BASE, user.getUsername()));
            Utility.executeOnWin(command);
            return this;
        }

        @Override
        public Builder assertUserExists(UserModel user)
        {
            command = String.format("%s dsquery user \"%s\"", psCommand, String.format(USER_SEARCH_BASE, user.getUsername()));
            //Assert.assertEquals(Utility.executeOnWin(command), String.format("[\"%s\"]", String.format(USER_SEARCH_BASE, user.getUsername())), "User was not found!");
            return this;
        }

        @Override
        public Builder assertUserDoesNotExist(UserModel user)
        {
            command = String.format("%s dsquery user \"%s\"", psCommand, String.format(USER_SEARCH_BASE, user.getUsername()));
            //Assert.assertEquals(Utility.executeOnWin(command), "[]", "User was found!");
            return this;
        }
    }
}

