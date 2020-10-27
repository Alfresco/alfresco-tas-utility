package org.alfresco.utility.data;

import static org.alfresco.utility.report.log.Step.STEP;

import org.alfresco.dataprep.AlfrescoHttpClient;
import org.alfresco.dataprep.AlfrescoHttpClientFactory;
import org.alfresco.dataprep.GroupService;
import org.alfresco.utility.constants.UserRole;
import org.alfresco.utility.exception.DataPreparationException;
import org.alfresco.utility.model.GroupModel;
import org.alfresco.utility.model.SiteModel;
import org.alfresco.utility.model.UserModel;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Data Preparation for Groups
 * 
 * @author Cristina Axinte
 */
@Service
@Scope(value = "prototype")
public class DataGroup extends TestData<DataGroup>
{
    private final GroupService groupService;

    @Autowired
    private AlfrescoHttpClientFactory alfrescoHttpClientFactory;

    public DataGroup(GroupService groupService)
    {
        this.groupService = groupService;
    }

    /**
     * Creates a new random group.
     * 
     * @return
     */
    public GroupModel createRandomGroup()
    {
        String groupName = RandomData.getRandomName("Group");
        STEP(String.format("DATAPREP: Creating group %s with admin", groupName));
        GroupModel groupModel = new GroupModel(groupName);
        groupModel = createGroup(groupModel);
        return groupModel;
    }

    /**
     * Creates a new group with admin user based on a group model
     * 
     * @return new created group
     */
    public GroupModel createGroup(GroupModel groupModel)
    {
        STEP(String.format("DATAPREP: Creating group %s with admin", groupModel.getDisplayName()));
        groupService.createGroup(getAdminUser().getUsername(), getAdminUser().getPassword(), groupModel.getDisplayName());
        return groupModel;
    }

    /**
     * Current user is added to the specified group
     * You can also use the {@link #usingUser(org.alfresco.utility.model.UserModel)}
     * method for defining a user to be added to the group
     * 
     * @return
     */
    public GroupModel addUserToGroup(GroupModel groupModel)
    {
        STEP(String.format("DATAPREP: Add user %s to group %s", getCurrentUser().getUsername(), groupModel.getDisplayName()));
        groupService.addUserToGroup(getAdminUser().getUsername(), getAdminUser().getPassword(), groupModel.getDisplayName(), getCurrentUser().getUsername());
        return groupModel;
    }

    public void addGroupToParentGroup(GroupModel parentGroup, GroupModel... groupsToAdd)
    {
        for(GroupModel groupModel : groupsToAdd)
        {
            STEP(String.format("DATAPREP: Add group %s to group %s", groupModel.getDisplayName(), parentGroup.getDisplayName()));
            boolean added = groupService.addSubGroup(getAdminUser().getUsername(), getAdminUser().getPassword(),
                parentGroup.getGroupIdentifier(), groupModel.getGroupIdentifier());
            if(!added)
            {
                LOG.error(String.format("Retry add group %s to group %s", groupModel.getDisplayName(), parentGroup.getDisplayName()));
                groupService.addSubGroup(getAdminUser().getUsername(), getAdminUser().getPassword(),
                        parentGroup.getGroupIdentifier(), groupModel.getGroupIdentifier());
            }
        }
    }

    /**
     * Adds list of users to the specified group
     * 
     * @param groupModel
     * @param users
     * @return
     */
    public GroupModel addListOfUsersToGroup(GroupModel groupModel, UserModel... users)
    {
        for (UserModel userModel : users)
        {
            STEP(String.format("DATAPREP: Add user %s to group %s", getCurrentUser().getUsername(), groupModel.getDisplayName()));
            usingUser(userModel).addUserToGroup(groupModel);
        }
        return groupModel;
    }

    /**
     * Delete group
     * 
     * @param groupModel GroupModel group to delete
     */
    public void deleteGroup(GroupModel groupModel) throws DataPreparationException
    {
        STEP(String.format("DATAPREP: Deleting %s group", groupModel.getGroupIdentifier()));
        groupService.removeGroup(getAdminUser().getUsername(), getAdminUser().getPassword(), groupModel.getGroupIdentifier());

    }

    /**
     * @param groupModel {@link GroupModel}
     * @param userToRemove {@link UserModel}
     */
    public void removeUserFromGroup(GroupModel groupModel, UserModel userToRemove) throws DataPreparationException
    {
        STEP(String.format("DATAPREP: Remove user %s from group %s", userToRemove.getUsername(), groupModel.getGroupIdentifier()));
        groupService.removeUserFromGroup(getAdminUser().getUsername(), getAdminUser().getPassword(), groupModel.getGroupIdentifier(),
            userToRemove.getUsername());
    }

    public void addGroupToSite(GroupModel groupToInvite, SiteModel site, UserRole role)
    {
        STEP(String.format("DATAPREP: Invite group '%s' to site %s with role %s", groupToInvite.getDisplayName(), site.getTitle(), role.toString()));
        groupService.inviteGroupToSite(getCurrentUser().getUsername(), getCurrentUser().getPassword(),
            site.getId(), groupToInvite.getGroupIdentifier(), role.toString());
    }

    public void updateGroupRole(GroupModel groupToUpdate, SiteModel site, UserRole role)
    {
        STEP(String.format("DATAPREP: Update group role '%s' to %s in site %s", groupToUpdate.getDisplayName(), role.toString(), site.getTitle()));
        groupService.changeGroupRole(getCurrentUser().getUsername(), getCurrentUser().getPassword(),
            site.getId(), groupToUpdate.getGroupIdentifier(), role.toString());
    }

    public void removeGroupFromSite(GroupModel groupToRemove, SiteModel site)
    {
        AlfrescoHttpClient client = alfrescoHttpClientFactory.getObject();

        String reqUrl = String.format(client.getApiUrl() + "sites/%s/memberships/GROUP_%s",
            site.getId().toLowerCase(), groupToRemove.getGroupIdentifier());
        HttpDelete deleteReq = new HttpDelete(reqUrl);
        HttpResponse response = client.executeAndRelease(getCurrentUser().getUsername(), getCurrentUser().getPassword(), deleteReq);
        if(HttpStatus.SC_OK == response.getStatusLine().getStatusCode())
        {
            LOG.info("Group {} was removed from site {}", groupToRemove.getDisplayName(), site.getId());
        }
        else
        {
            LOG.error("Failed to remove group {} from site {}. Error: {}", groupToRemove.getDisplayName(),
                site.getId(), response.toString());
        }
    }
}
