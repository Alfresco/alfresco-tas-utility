package org.alfresco.utility.data.auth;

import javax.naming.NamingException;

import org.alfresco.utility.model.GroupModel;
import org.alfresco.utility.model.UserModel;

/**
 * Created by Claudia Agache on 6/14/2017.
 */
public interface GroupManageable
{
    GroupManageable createGroup(GroupModel group) throws NamingException;

    GroupManageable deleteGroup(GroupModel group) throws NamingException;

    GroupManageable addUserToGroup(UserModel user, GroupModel group) throws NamingException;

    GroupManageable removeUserFromGroup(UserModel user, GroupModel group) throws NamingException;

    GroupManageable assertGroupExists(GroupModel group)throws NamingException;

    GroupManageable assertGroupDoesNotExist(GroupModel group) throws NamingException;

    GroupManageable assertUserIsMemberOfGroup(UserModel user, GroupModel group) throws NamingException;

    GroupManageable assertUserIsNotMemberOfGroup(UserModel user, GroupModel group) throws NamingException;

}
