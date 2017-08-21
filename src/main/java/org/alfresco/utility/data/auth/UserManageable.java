package org.alfresco.utility.data.auth;

import org.alfresco.utility.exception.TestStepException;
import org.alfresco.utility.model.UserModel;

import javax.naming.NamingException;
import java.util.HashMap;

/**
 * Interface for any authentication mechanism like LDAP, LDAP-AD, Kerberos, etc.
 * 
 * @author Paul Brodner
 */
public interface UserManageable
{
    UserManageable createUser(UserModel user) throws NamingException;

    UserManageable deleteUser(UserModel user) throws NamingException;

    UserManageable updateUser(UserModel user, HashMap<String, String> attributes) throws NamingException;

    UserManageable assertUserExists(UserModel user) throws NamingException;

    UserManageable assertUserDoesNotExist(UserModel user) throws NamingException, TestStepException;
}
