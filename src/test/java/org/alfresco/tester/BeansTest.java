package org.alfresco.tester;

import org.alfresco.dataprep.UserService;
import org.alfresco.tester.data.DataUser;
import org.alfresco.tester.exception.DataPreparationException;
import org.alfresco.tester.model.UserModel;
import org.alfresco.tester.report.ReportListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
 * Unit testing bean configurations
 * 
 * @author Paul Brodner
 */
@ContextConfiguration("classpath:alfresco-tester-context.xml")
@Listeners(value = ReportListenerAdapter.class)
public class BeansTest extends AbstractTestNGSpringContextTests
{
    @Autowired
    protected TasProperties properties;

    @Autowired
    protected UserService userService;

    @Autowired
    protected DataUser dataUser;

    @Autowired
    protected ServerHealth serverHealth;

    @BeforeClass
    public void checkServerHealth() throws Exception
    {
        serverHealth.assertIfServerOnline();
    }

    @Test
    public void getEnvPropertiesBean()
    {
        Assert.assertNotNull(properties, "Bean EnvProperties is initialised");
    }

    @Test
    public void getAdminUsername()
    {
        Assert.assertEquals(properties.getAdminUser(), "admin");
    }

    @Test
    public void getAdminPassword()
    {
        Assert.assertEquals(properties.getAdminUser(), "admin");
    }

    @Test
    public void getUserServiceBean()
    {
        Assert.assertNotNull(properties, "Bean UserService is initialised");
    }

    @Test
    public void getDataUserBean()
    {
        Assert.assertNotNull(dataUser, "Bean DataUser is initialised");
    }

    // @Test
    public void createNewUser() throws DataPreparationException
    {
        UserModel newUser = dataUser.createUser(DataUser.getRandomAlphanumeric());
        dataUser.assertUserExist(newUser);
    }
}
