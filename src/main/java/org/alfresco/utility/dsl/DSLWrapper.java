package org.alfresco.utility.dsl;

import org.alfresco.dataprep.ContentService;
import org.alfresco.utility.JmxClient;
import org.alfresco.utility.LogFactory;
import org.alfresco.utility.data.DataContent;
import org.alfresco.utility.data.DataValue;
import org.alfresco.utility.model.UserModel;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Client Wrapper
 *
 * @param <Client>
 */
public abstract class DSLWrapper<Client>
{
    @Autowired
    DataContent dataContent;
    
    @Autowired
    protected ContentService contentService;

    @Autowired
    protected JmxClient jmxClient;

    protected Logger LOG = LogFactory.getLogger();

    /**
     * This is the test user used in test
     * It should be defined in the {@link #authenticateUser(UserModel)} method
     */
    private UserModel testUser = new UserModel(DataValue.UNDEFINED.toString(), DataValue.UNDEFINED.toString());

    // HELPERS ----------------------------------------------------------
   
    

    /**
     * @return test user. This should be defined in {@link #authenticateUser(UserModel)} method
     */
    public UserModel getTestUser()
    {
        return testUser;
    }

    /**
     * Define the test user.
     * This should be initialized in {@link #authenticateUser(UserModel)} method
     * 
     * @param testUser
     */
    public void setTestUser(UserModel testUser)
    {
        this.testUser = testUser;
    }


   
}
