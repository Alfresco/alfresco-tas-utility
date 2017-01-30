package org.alfresco.utility.data;

import static org.alfresco.utility.report.log.Step.STEP;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.alfresco.dataprep.UserService;
import org.alfresco.utility.TasProperties;
import org.alfresco.utility.constants.UserRole;
import org.alfresco.utility.exception.DataPreparationException;
import org.alfresco.utility.model.ContentModel;
import org.alfresco.utility.model.SiteModel;
import org.alfresco.utility.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.testng.Assert;

/**
 * Data Preparation for Users
 */
@Service
@Scope(value = "prototype")
public class DataUser extends TestData<DataUser>
{
    @Autowired
    private UserService userService;

    static String USER_NOT_CREATED = "User %s  not created";

    /**
     * Creates a new user with a specific user name on test server defined in {@link TasProperties}
     * file.
     * If no user is specified with {@link #usingUser(UserModel)} then the random user is created with admin.
     * This user will have default password set to "password"
     * 
     * @param userName
     * @return
     * @throws DataPreparationException
     */
    public UserModel createUser(String userName) throws DataPreparationException
    {
        return createUser(userName, PASSWORD);
    }

    /**
     * Create a new user based on the {@link UserModel} provided
     * 
     * @param user
     * @return {@link UserModel}
     * @throws DataPreparationException
     */
    public UserModel createUser(UserModel user) throws DataPreparationException
    {
        return createUser(user.getUsername(), user.getPassword());
    }

    /**
     * Creates a new random user with a specific user name on test server defined in {@link TasProperties}
     * file.
     * If no user is specified with {@link #usingUser(UserModel)} then the random user is created with admin
     * 
     * @param userName
     * @return
     * @throws DataPreparationException
     */
    public UserModel createUser(String userName, String password) throws DataPreparationException
    {
        UserModel newUser = new UserModel(userName, password);
        STEP(String.format("DATAPREP: Creating %s user", newUser.getUsername()));
        LOG.info("Create user {}", newUser.toString());

        Boolean created = userService.create(getAdminUser().getUsername(), getAdminUser().getPassword(), userName, password, String.format(EMAIL, userName),
                String.format("%s FirstName", userName), String.format("LN-%s", userName));
        if (!created)
            throw new DataPreparationException(String.format(USER_NOT_CREATED, newUser.toString()));

        newUser.setDomain(getCurrentUser().getDomain());
        return newUser;
    }

    /**
     * Creates a new random user with a specific user name on test server defined in {@link TasProperties}
     * file.
     * The user is created with user specified with {@link #usingUser(UserModel)}
     * 
     * @param userName
     * @return
     * @throws DataPreparationException
     */
    public UserModel createUserWithTenant(String userName) throws DataPreparationException
    {
        UserModel newUser = new UserModel(userName, PASSWORD);
        newUser.setDomain(getCurrentUser().getDomain());

        STEP(String.format("DATAPREP: Creating %s tenant user", newUser.getUsername()));
        Boolean created = userService.create(getCurrentUser().getUsername(), getCurrentUser().getPassword(), userName, PASSWORD, String.format(EMAIL, userName),
                String.format("%s FirstName", userName), String.format("LN-%s", userName));
        if (!created)
            throw new DataPreparationException(String.format(USER_NOT_CREATED, newUser.toString()));

        return newUser;
    }

    /**
     * Creates a new random user.
     * If no user is specified with {@link #usingUser(UserModel)} then the random user is created with admin
     * 
     * @return
     * @throws DataPreparationException
     */
    public UserModel createRandomTestUser() throws DataPreparationException
    {
        String user = RandomData.getRandomName("User");
        return createUser(user);
    }

    /**
     * Creates a new random user using a prefix for user name.
     * If no user is specified with {@link #usingUser(UserModel)} then the random user is created with admin
     * 
     * @return
     * @throws DataPreparationException
     */
    public UserModel createRandomTestUser(String prefix) throws DataPreparationException
    {
        String user = RandomData.getRandomName(prefix);
        return createUser(user);
    }

    public void addUserToSite(UserModel userModel, SiteModel siteModel, UserRole role)
    {        
        STEP(String.format("DATAPREP: Adding [%s] user with role [%s] role to [%s] site", userModel.getUsername(), role.toString(), siteModel.getId()));
        userService.createSiteMember(getCurrentUser().getUsername(), getCurrentUser().getPassword(), userModel.getUsername(), siteModel.getId(),
                role.toString());

        userModel.setUserRole(role);
    }

    public ListUserWithRoles addUsersWithRolesToSite(SiteModel siteModel, UserRole... roles) throws DataPreparationException
    {
        ListUserWithRoles usersWithRoles = new ListUserWithRoles();
        for (UserRole role : roles)
        {
            UserModel userModel = createRandomTestUser();            
            addUserToSite(userModel, siteModel, role);
            usersWithRoles.add(userModel);
        }

        return usersWithRoles;
    }

    public void assertUserExist(UserModel user)
    {
        assertUserExist(user.getUsername());
    }

    public void assertUserExist(String username)
    {
        STEP(String.format("DATAPREP: Assert that %s user exists",username));
        LOG.info("Check user {} exist in repository", username.toString());
        Assert.assertTrue(isUserInRepo(username), String.format("User {} exist in repository", username));
    }

    public void assertUserDoesNotExist(UserModel user)
    {
        STEP(String.format("DATAPREP: Assert that %s user does not exist ",user.getUsername()));
        LOG.info("Check user {} does not exist in repository", user.toString());
        Assert.assertFalse(isUserInRepo(user.getUsername()), String.format("User {} exist in repository", user.toString()));
    }

    /**
     * Check if user exist in repository
     * 
     * @param username
     * @return boolean
     */
    public boolean isUserInRepo(String username)
    {
        return userService.userExists(tasProperties.getAdminUser(), tasProperties.getAdminPassword(), username);
    }

    /**
     * Handle list of user with particular roles
     */
    public class ListUserWithRoles
    {
        private List<UserModel> usersWithRoles;

        public ListUserWithRoles()
        {
            usersWithRoles = new ArrayList<UserModel>();
        }

        public void add(UserModel userModel)
        {
            this.usersWithRoles.add(userModel);
        }

        /**
         * Return one user that has the role specified
         * 
         * @param userRole
         * @return
         */
        public UserModel getOneUserWithRole(UserRole userRole)
        {
            UserModel userModel = null;
            for (UserModel user : this.usersWithRoles)
            {
                if (user.getUserRole().equals(userRole))
                    return user;
            }
            return userModel;
        }
    }

    /**
     * Delete user
     * 
     * @param userToDelete UserModel user delete
     * @throws DataPreparationException
     */
    public void deleteUser(UserModel userToDelete) throws DataPreparationException
    {
        STEP(String.format("DATAPREP: Deleting %s user",userToDelete.getUsername()));
        LOG.info("Delete user {}", userToDelete.getUsername());
        boolean deleted = userService.delete(getAdminUser().getUsername(), getAdminUser().getPassword(), userToDelete.getUsername());
        if (!deleted)
        {
            throw new DataPreparationException(String.format("Failed to delete user '%s'.", userToDelete.getUsername()));
        }
    }
    
    /**
     * Disable user
     * 
     * @param userToDisable
     * @throws DataPreparationException
     */
    public void disableUser(UserModel userToDisable) throws DataPreparationException
    {
        STEP(String.format("DATAPREP: Disable %s user",userToDisable.getUsername()));
        boolean disabled = userService.disableUser(getCurrentUser().getUsername(), getCurrentUser().getPassword(), userToDisable.getUsername(), true);
        if (!disabled)
        {
            throw new DataPreparationException(String.format("Failed to disable user '%s'.", userToDisable.getUsername()));
        }
    }
    
    /**
     * Verify if content exists in trash can
     * 
     * @param content {@link ContentModel} content to verify
     */
    public void assertTrashCanHasContent(ContentModel... contents)
    {
        List<String> nodes = userService.getItemsNodeRefFromTrashcan(getCurrentUser().getUsername(),getCurrentUser().getPassword());
        List<String> matches = new ArrayList<String>();
        for(ContentModel content: contents)
        {
            matches.clear();
            STEP(String.format("DATAPREP: Verify if %s is in trash can", content.getName()));
            matches = nodes.stream().filter(it -> it.contains(content.getNodeRef().split(";")[0])).collect(Collectors.toList());
            Assert.assertFalse(matches.isEmpty(), String.format("Item %s found in trash can", content.getName()));
        }
    }
    
    /**
     * Verify that content does not exist in trash can
     * 
     * @param content {@link ContentModel} content to verify
     */
    public void assertTrashCanDoesNotHaveContent(ContentModel... contents)
    {
        List<String> nodes = userService.getItemsNodeRefFromTrashcan(getCurrentUser().getUsername(),getCurrentUser().getPassword());
        List<String> matches = new ArrayList<String>();
        for(ContentModel content: contents)
        {
            matches.clear();
            STEP(String.format("DATAPREP: Verify if %s is not in trash can", content.getName()));
            matches = nodes.stream().filter(it -> it.contains(content.getNodeRef().split(";")[0])).collect(Collectors.toList());
            Assert.assertTrue(matches.isEmpty(), String.format("Item %s found in trash can", content.getName()));
        }
    }
    
    /**
     * Set user quota in MB
     * 
     * @param userToModify
     * @param quota
     * @throws DataPreparationException
     */
    public void setUserQuota(UserModel userToModify, int quota) throws DataPreparationException
    {
        STEP(String.format("DATAPREP: Set %d MB quota to %s user", quota, userToModify.getUsername()));
        boolean status = userService.setUserQuota(getCurrentUser().getUsername(), getCurrentUser().getPassword(), userToModify.getUsername(), quota);
        if (!status)
        {
            throw new DataPreparationException(String.format("Failed to set quota to user '%s'.", userToModify.getUsername()));
        }
    }
}