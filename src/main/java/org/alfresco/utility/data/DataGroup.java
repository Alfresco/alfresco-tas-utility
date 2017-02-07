package org.alfresco.utility.data;

import static org.alfresco.utility.report.log.Step.STEP;

import org.alfresco.dataprep.GroupService;
import org.alfresco.utility.exception.DataPreparationException;
import org.alfresco.utility.model.GroupModel;
import org.alfresco.utility.model.UserModel;
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
    @Autowired
    private GroupService groupService;

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
     * @throws DataPreparationException
     */
    public void deleteGroup(GroupModel groupModel) throws DataPreparationException
    {
        STEP(String.format("DATAPREP: Deleting %s group", groupModel.getGroupIdentifier()));
        boolean deleted = groupService.removeGroup(getAdminUser().getUsername(), getAdminUser().getPassword(), groupModel.getGroupIdentifier());
        if (!deleted)
        {
            throw new DataPreparationException(String.format("Failed to delete group '%s'.", groupModel.getGroupIdentifier()));
        }
    }

    /**
     * @param groupModel {@link GroupModel}
     * @param userToRemove {@link UserModel}
     */
    public void removeUserFromGroup(GroupModel groupModel, UserModel userToRemove) throws DataPreparationException
    {
        STEP(String.format("DATAPREP: Remove user %s from group %s", userToRemove.getUsername(), groupModel.getGroupIdentifier()));
        boolean removed = groupService.removeUserFromGroup(getAdminUser().getUsername(), getAdminUser().getPassword(), groupModel.getGroupIdentifier(),
                userToRemove.getUsername());
        if (!removed)
        {
            throw new DataPreparationException(
                    String.format("Failed to remove %s from '%s' group.", userToRemove.getUsername(), groupModel.getGroupIdentifier()));
        }
    }
}
