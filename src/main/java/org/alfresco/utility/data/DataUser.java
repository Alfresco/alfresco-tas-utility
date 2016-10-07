package org.alfresco.utility.data;

import java.util.ArrayList;
import java.util.List;

import org.alfresco.dataprep.UserService;
import org.alfresco.utility.TasProperties;
import org.alfresco.utility.constants.UserRole;
import org.alfresco.utility.exception.DataPreparationException;
import org.alfresco.utility.model.SiteModel;
import org.alfresco.utility.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.testng.Assert;

/**
 * Data Preparation for Users
 * 
 */
@Service
@Scope(value = "prototype")
public class DataUser extends TestData<DataUser>
{
    @Autowired
    private UserService userService;

    static String USER_NOT_CREATED = "User %s  not created";

    /**
     * Creates a new random user with a specific user name on test server defined in {@link TasProperties}
     * file.
     * If no user is specified with {@link #usingUser(UserModel)} then the random user is created with admin
     * 
     * @param userName
     * @return
     * @throws DataPreparationException
     */
    public UserModel createUser(String userName) throws DataPreparationException
    {
        UserModel newUser = new UserModel(userName, PASSWORD);
        LOG.info("Create user {}", newUser.toString());
        
        Boolean created = userService.create(getAdminUser().getUsername(), 
                                            getAdminUser().getPassword(), 
                                            userName, PASSWORD, String.format(EMAIL, userName),
                                            String.format("%s FirstName", userName), 
                                            String.format("LN-%s", userName));
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
        LOG.info("Create user {}", newUser.toString());
        
        Boolean created = userService.create(getCurrentUser().getUsername(), 
                                            getCurrentUser().getPassword(), 
                                            userName, PASSWORD, String.format(EMAIL, userName),
                                            String.format("%s FirstName", userName), 
                                            String.format("LN-%s", userName));
        if (!created)
            throw new DataPreparationException(String.format(USER_NOT_CREATED, newUser.toString()));

        newUser.setDomain(getCurrentUser().getDomain());
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
        return createRandomTestUser("User");
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
        return createUser(RandomData.getRandomName(prefix));
    }
    
    public void addUserToSite(UserModel userModel, SiteModel siteModel, UserRole role)
    {
        userService.createSiteMember(getCurrentUser().getUsername(), getCurrentUser().getPassword(),
                userModel.getUsername(), siteModel.getId(), role.toString());
        
        userModel.setUserRole(role);
    }
    
    public ListUserWithRoles addUsersWithRolesToSite(SiteModel siteModel, UserRole... roles) throws DataPreparationException
    {
        ListUserWithRoles usersWithRoles = new ListUserWithRoles();
    	for(UserRole role: roles)
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
        LOG.info("Check user {} exist in repository", username.toString());
        Assert.assertTrue(userService.userExists(
                            tasProperties.getAdminUser(), 
                            tasProperties.getAdminPassword(), 
                            username),
                            String.format("User {} exist in repository", username));  
    }

    public void assertUserDoesNotExist(UserModel user)
    {
        LOG.info("Check user {} does not exist in repository", user.toString());
        Assert.assertFalse(userService.userExists(
                              tasProperties.getAdminUser(), 
                              tasProperties.getAdminPassword(), 
                              user.getUsername()),
                String.format("User {} exist in repository", user.toString()));
    }
    
    /**
     * 
     * Handle list of user with particular roles
     *
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
         * @param userRole
         * @return
         */
        public UserModel getOneUserWithRole(UserRole userRole)
        {
           UserModel userModel = null; 
           for(UserModel user :  this.usersWithRoles)
           {
               if(user.getUserRole().equals(userRole))
                   return user;
           }
           return userModel;
        }
    }
            
}