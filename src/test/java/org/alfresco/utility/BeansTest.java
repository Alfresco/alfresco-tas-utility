package org.alfresco.utility;

import org.alfresco.dataprep.CMISUtil.DocumentType;
import org.alfresco.dataprep.UserService;
import org.alfresco.utility.data.DataContent;
import org.alfresco.utility.data.DataSite;
import org.alfresco.utility.data.DataUser;
import org.alfresco.utility.model.FileModel;
import org.alfresco.utility.model.FolderModel;
import org.alfresco.utility.model.SiteModel;
import org.alfresco.utility.model.UserModel;
import org.alfresco.utility.network.ModelAndMessagesConsole;
import org.alfresco.utility.network.ServerHealth;
import org.alfresco.utility.network.TenantConsole;
import org.alfresco.utility.network.WorkflowConsole;
import org.alfresco.utility.report.HtmlReportListener;
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
@Listeners(value = HtmlReportListener.class)
public class BeansTest extends AbstractTestNGSpringContextTests
{
    @Autowired
    protected TasProperties properties;

    @Autowired
    protected UserService userService;

    @Autowired
    protected DataUser dataUser;

    @Autowired
    protected DataSite dataSite;

    @Autowired
    protected DataContent dataContent;

    @Autowired
    protected ServerHealth serverHealth;
    
    @Autowired
    TenantConsole alfrescoTenantConsole;
    
    @Autowired
    WorkflowConsole  workflowConsole;
    
    @Autowired
    ModelAndMessagesConsole modelAndMessagesConsole;

    SiteModel siteModel;

    @BeforeClass
    public void checkServerHealth() throws Exception
    {
        serverHealth.assertServerIsOnline();
        siteModel = dataSite.createPublicRandomSite();
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

    @Test
    public void createFolderAndContent() throws Exception
    {
        FolderModel newFolder = dataContent.usingAdmin().usingSite(siteModel).createFolder();
        dataContent.usingResource(newFolder).assertContentExist();

        FileModel newFile = dataContent.createContent(DocumentType.MSEXCEL);
        dataContent.usingResource(newFile).assertContentExist();
    }

    @Test
    public void createFolderAndContentInSpaces() throws Exception
    {
        FolderModel newFolder;

        UserModel newUser = dataUser.createRandomTestUser();
        newFolder = dataContent.usingUser(newUser).usingUserHome().createFolder();
        dataContent.usingResource(newFolder).assertContentExist();
    }     
    
    @Test
    public void testServerlogs() throws Exception
    {
        dataUser.usingLastServerLogLines(100).assertLogLineIs("DEBUG");
    }
    
    @Test
    public void testConsole() throws Exception
    {
        alfrescoTenantConsole.tenantExist();
        System.out.println(workflowConsole.user());
        System.out.println(modelAndMessagesConsole.showModels());
    }
    
}
