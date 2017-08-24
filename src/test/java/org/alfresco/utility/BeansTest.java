package org.alfresco.utility;

import java.util.HashMap;

import javax.naming.NamingException;

import org.alfresco.dataprep.CMISUtil.DocumentType;
import org.alfresco.dataprep.UserService;
import org.alfresco.utility.data.DataContent;
import org.alfresco.utility.data.DataSite;
import org.alfresco.utility.data.DataUser;
import org.alfresco.utility.data.auth.DataKerberos;
import org.alfresco.utility.data.auth.DataLDAP;
import org.alfresco.utility.data.auth.DataNtlmPassthru;
import org.alfresco.utility.data.auth.DataOpenLDAP;
import org.alfresco.utility.data.auth.DataOracleDirectoryServer;
import org.alfresco.utility.exception.DataPreparationException;
import org.alfresco.utility.exception.TestStepException;
import org.alfresco.utility.model.FileModel;
import org.alfresco.utility.model.FolderModel;
import org.alfresco.utility.model.GroupModel;
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

    @Autowired
    protected DataOracleDirectoryServer oracleAuth;

    @Autowired
    protected DataNtlmPassthru ntlmPassthruAuth;

    @Autowired
    protected DataLDAP ldapAuth;

    @Autowired
    protected DataOpenLDAP openLdapAuth;

    @Autowired
    protected DataKerberos dataKerberos;
    
    @BeforeClass
    public void checkServerHealth() throws Exception
    {
//        serverHealth.assertServerIsOnline();
//        siteModel = dataSite.createPublicRandomSite();
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

    @Test(enabled=false)
    public void createFolderAndContent() throws Exception
    {
        FolderModel newFolder = dataContent.usingAdmin().usingSite(siteModel).createFolder();
        dataContent.usingResource(newFolder).assertContentExist();

        FileModel newFile = dataContent.createContent(DocumentType.MSEXCEL);
        dataContent.usingResource(newFile).assertContentExist();
    }

    @Test(enabled=false)
    public void createFolderAndContentInSpaces() throws Exception
    {
        FolderModel newFolder;

        UserModel newUser = dataUser.createRandomTestUser();
        newFolder = dataContent.usingUser(newUser).usingUserHome().createFolder();
        dataContent.usingResource(newFolder).assertContentExist();
    }     
    
    @Test(enabled=false)
    public void testServerlogs() throws Exception
    {
        dataUser.usingLastServerLogLines(100).assertLogLineIs("DEBUG");
    }
    
    @Test(enabled=false)
    public void testConsole() throws Exception
    {
        alfrescoTenantConsole.tenantExist();
        System.out.println(workflowConsole.user());
        System.out.println(modelAndMessagesConsole.showModels());
    }

    @Test(enabled=false)
    public void testOracleAuth() throws Exception {
        HashMap<String, String> newUserAttributes = new HashMap<>();
        newUserAttributes.put("sn", "new lastName");
        newUserAttributes.put("userPassword", "newPassword");
        UserModel userModel = UserModel.getRandomUserModel();
        oracleAuth.perform()
                .createUser(userModel)
                .assertUserExists(userModel)
                .updateUser(userModel, newUserAttributes)
                .deleteUser(userModel)
                .assertUserDoesNotExist(userModel);
    }

    @Test(enabled=false)
    public void testNtlmPassthruAuth() throws Exception {
        HashMap<String, String> newUserAttributes = new HashMap<>();
        newUserAttributes.put("ln", "new lastName");
        newUserAttributes.put("pwd", "newPassword");
        UserModel userModel = UserModel.getRandomUserModel();
        ntlmPassthruAuth.perform()
                .createUser(userModel)
                .assertUserExists(userModel)
                .updateUser(userModel, newUserAttributes)
                .disableUser(userModel)
                .enableUser(userModel)
                .deleteUser(userModel)
                .assertUserDoesNotExist(userModel);
    }

    @Test(enabled=false)
    public void testLDAPAuthUser() throws NamingException, DataPreparationException, TestStepException {
        HashMap<String, String> newUserAttributes = new HashMap<>();
        newUserAttributes.put("sn", "new lastName");
        newUserAttributes.put("userPassword", "newPassword");
        UserModel userModel = UserModel.getRandomUserModel();
        ldapAuth.perform()
                .createUser(userModel)
                .assertUserExists(userModel)
                .updateUser(userModel, newUserAttributes)
                .enableUser(userModel)
                .assertUserIsEnabled(userModel, DataLDAP.UserAccountControlValue.enabledPasswordNotRequired)
                .disableUser(userModel)
                .assertUserIsDisabled(userModel, DataLDAP.UserAccountControlValue.disabledPasswordNotRequired)
                .deleteUser(userModel)
                .assertUserDoesNotExist(userModel);
    }

    @Test(enabled=false)
    public void testLADPAuthUserCreation() throws NamingException
    {
        UserModel enabledUser = UserModel.getRandomUserModel();
        UserModel disabledUser = UserModel.getRandomUserModel();
        ldapAuth.perform()
                .createEnabledUserPasswordNotRequired(enabledUser)
                .createDisabledUser(disabledUser)
                .assertUserExists(enabledUser)
                .assertUserExists(disabledUser)
                .assertUserIsDisabled(disabledUser, DataLDAP.UserAccountControlValue.disabled)
                .assertUserIsEnabled(enabledUser, DataLDAP.UserAccountControlValue.enabledPasswordNotRequired);
    }

    @Test(enabled=false)
    public void testLDAPAuthGroup () throws NamingException
    {
        GroupModel newGroup = GroupModel.getRandomGroupModel();
        UserModel newUser = UserModel.getRandomUserModel();
        ldapAuth.perform()
                .createGroup(newGroup)
                .createUser(newUser)
                .assertGroupExists(newGroup)
                .addUserToGroup(newUser, newGroup)
                .assertUserIsMemberOfGroup(newUser, newGroup)
                .removeUserFromGroup(newUser, newGroup)
                .assertUserIsNotMemberOfGroup(newUser, newGroup)
                .deleteGroup(newGroup)
                .assertGroupDoesNotExist(newGroup);
    }

    @Test(enabled=false)
    public void testOpenLDAPAuthUser() throws Exception {
        HashMap<String, String> newUserAttributes = new HashMap<>();
        newUserAttributes.put("sn", "new lastNamem");
        newUserAttributes.put("userPassword", "newPassword123");
        UserModel userModel = UserModel.getRandomUserModel();
        openLdapAuth.perform()
                .createUser(userModel)
                .assertUserExists(userModel)
                .updateUser(userModel, newUserAttributes)
                .deleteUser(userModel)
                .assertUserDoesNotExist(userModel);
    }

    @Test(enabled=false)
    public void testOpenLDAPAuthGroup () throws NamingException
    {
        GroupModel newGroup = GroupModel.getRandomGroupModel();
        UserModel newUser = UserModel.getRandomUserModel();
        openLdapAuth.perform()
                .createUser(newUser)
                .createGroup(newGroup)
                .assertGroupExists(newGroup)
                .addUserToGroup(newUser, newGroup)
                .assertUserIsMemberOfGroup(newUser, newGroup)
                .removeUserFromGroup(newUser, newGroup)
                .assertUserIsNotMemberOfGroup(newUser, newGroup)
                .deleteGroup(newGroup)
                .assertGroupDoesNotExist(newGroup);
    }

    @Test(enabled=false)
    public void testKerberosUser() throws Exception {
        UserModel testUser = UserModel.getRandomUserModel();
        HashMap<String, String> newUserAttributes = new HashMap<>();
        newUserAttributes.put("sn", "updatedUser");
        dataKerberos.perform()
                .createUser(testUser)
                .assertUserExists(testUser)
                .updateUser(testUser, newUserAttributes)
                .deleteUser(testUser)
                .assertUserDoesNotExist(testUser);
    }
}
