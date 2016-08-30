package org.alfresco.utility.data;

import org.alfresco.dataprep.UserService;
import org.alfresco.utility.TasProperties;
import org.alfresco.utility.exception.DataPreparationException;
import org.alfresco.utility.model.SiteModel;
import org.alfresco.utility.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.alfresco.api.entities.Role;
import org.springframework.stereotype.Service;
import org.testng.Assert;

/**
 * Data Preparation for Users
 * 
 * @author Paul Brodner
 */
@Service
public class DataUser extends TestData<DataUser>
{
    @Autowired
    private UserService userService;

    static String USER_NOT_CREATED = "User %s  not created";

    /**
     * Creates a new random user on test server defined in {@link TasProperties}
     * file.
     * 
     * @param userName
     * @return
     * @throws DataPreparationException
     */
    public UserModel createUser(String userName) throws DataPreparationException
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

        return newUser;
    }

    public UserModel createRandomTestUser() throws DataPreparationException
    {
        return createRandomTestUser("");
    }

    public UserModel createRandomTestUser(String prefix) throws DataPreparationException
    {
        return createUser(getRandomName(prefix));
    }

    
    
    public void addUserToSite(UserModel userModel, SiteModel siteModel, Role role)
    {
        userService.createSiteMember(tasProperties.getAdminUser(), tasProperties.getAdminPassword(), 
                userModel.getUsername(), siteModel.getId(), role.toString());
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
}