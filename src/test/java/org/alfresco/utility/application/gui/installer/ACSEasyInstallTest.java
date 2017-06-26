package org.alfresco.utility.application.gui.installer;

import org.alfresco.utility.exception.CouldNotFindApplicationActionImage;
import org.alfresco.utility.model.TestGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration("classpath:alfresco-tester-context.xml")
public class ACSEasyInstallTest extends AbstractTestNGSpringContextTests
{
    @Autowired
    ACSInstaller installer;

    @Test(groups={"demo", TestGroup.OS_MAC}, priority=0)
    public void openAlfrescoInstallerWizard() throws Exception
    {
        installer.open();
    }
    
    @Test(groups={"demo"}, priority=1)
    public void waitForLanguageSelectionDialogToOpen() throws Exception
    {
        installer.waitForInstallerToOpen();
    }
    
    @Test(groups={"demo"}, priority=2)
    public void selectEnglishLanguage() throws Exception
    {
        installer.onLanguageSelectionDialog().clickOK();
    }
    
    @Test(groups={"demo"}, priority=3)
    public void onWelcomeSetupClickNext() throws Exception
    {
        installer.onSetup().clickNext();
    }
    
    @Test(groups={"demo"}, priority=4)
    public void acceptLicenseAgreement() throws Exception
    {
        installer.onLicensePage().acceptTheAgreement();
        installer.onSetup().clickNext();               
    }
    
    @Test(groups={"demo"}, priority=5)
    public void chooseEasyInstall() throws Exception
    {
        installer.onInstallationTypePage().chooseEasyInstall();
        installer.onSetup().clickNext();                
    }
    
    @Test(groups={"demo"}, priority=6)
    public void setInstallerDestinationPath() throws CouldNotFindApplicationActionImage, Exception
    {
        installer.onInstallationFolderPage()
                .setDestination(installer.getFileProperties().getInstallerDestinationPath().getPath());
        installer.onSetup().clickNext();
    }
    
    @Test(groups={"demo"}, priority=7)
    public void setDabaseServerPort() throws CouldNotFindApplicationActionImage, Exception
    {
        installer.onDatabaseParametersPage()
                 .setPort(installer.getFileProperties().getInstallerDBPort());
        
        installer.onSetup().clickNext();
    }
    
    @Test(groups={"demo"}, priority=8)
    public void setAdminPassword() throws CouldNotFindApplicationActionImage, Exception
    {
        installer.onAdminPasswordPage()
                 .setAdminPassword(installer.getFileProperties().getAdminPassword())
                 .setRepeatAdminPassword(installer.getFileProperties().getAdminPassword());
        
        installer.onSetup().clickNext();
    }
    
    
    @Test(groups={"demo"}, enabled = false)
    public void testInstallerInWindows() throws Exception
    {
        //installer.open();
        //installer.waitForInstallerToOpen().clickOK();        
       /* installer.onSetup().clickNext();
        installer.onLicensePage().acceptTheAgreement();
        installer.onSetup().clickNext();             
        
        installer.onInstallationTypePage().chooseAdvancedInstall().chooseEasyInstall().chooseAdvancedInstall();
        installer.onSetup().clickNext();

        installer.onSelectComponentsPage().checkJava()
                .checkLibreOffice()
                .checkPostgreSQL()
                .checkSolr1()
                .checkSolr4()
                .checkAlfrescoOfficeServices()
                .checkWebQuickStart()
                .checkGoogleDocsIntegration();
        installer.onSetup().clickNext();

        installer.onInstallationFolderPage().setDestination();
        installer.onSetup().clickNext();

        installer.onDatabaseConfigurationPage().setUsername()
                .setPassword().verifyPassword();
        installer.onSetup().clickNext();

       installer.onTomcatPortConfigurationPage()
               .setWebServerDomain()
               .setTomcatServerPort()
               .setTomcatShutdownPort()
               .setTomcatSSLPort()
               .setTomcatAJPPort();

       installer.onShardedSolrInstallationPage().setYes().setNo().setYes();
       installer.onSetup().clickNext();

       installer.onFtpPortPage().setFtpPort();
       installer.onSetup().clickNext();
       installer.onRmiPortPage().setRmiPort();
       installer.onSetup().clickNext();
       installer.onServiceStartupConfigurationPage().setAuto().setManual().setAuto().setManual();
       installer.onSetup().clickNext();

       installer.onLibreOfficeServerPortPage().setRmiPort();
       installer.onSetup().clickNext();

       installer.onRemoteSolrConfigurationPage().setRemoteSolrHost().setRemoteSolrSSLPort();
       installer.onSetup().clickNext(); */
    }
}