package org.alfresco.utility.data.auth;

import java.util.HashMap;

import javax.naming.NamingException;

import org.alfresco.utility.exception.TestStepException;
import org.alfresco.utility.model.UserModel;

/**
 * Interface for any authentication mechanism like LDAP, LDAP-AD, Kerberos, etc.
 * 
 * @author Paul Brodner
 */
public interface UserManageable
{
    UserManageable createUser(UserModel user) throws NamingException, Exception;

    UserManageable deleteUser(UserModel user) throws NamingException, Exception;

    UserManageable updateUser(UserModel user, HashMap<String, String> attributes) throws NamingException, Exception;

    UserManageable assertUserExists(UserModel user) throws NamingException;

    UserManageable assertUserDoesNotExist(UserModel user) throws NamingException, TestStepException;
}
