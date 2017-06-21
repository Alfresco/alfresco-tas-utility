package org.alfresco.utility.application.gui.installer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration("classpath:alfresco-tester-context.xml")
public class ACSInstallerTest extends AbstractTestNGSpringContextTests
{

    @Autowired
    ACSInstaller installer;

    @Test(groups={"demo"})
    public void testInstallerInMAC() throws Exception
    {
//        installer.open();
//        installer.waitForInstallerToOpen()
//                .clickCancel();
//
//        installer.open();
//        installer.waitForInstallerToOpen()
//                  .clickOK()
//                  .clickCancel();
//        installer.onDialog().clickYes();

        installer.open();
        installer.waitForInstallerToOpen()
                .clickOK()
                .clickCancel();
        installer.onDialog().clickNo();
        installer.onSetup().clickNext();
         
//        installer.onLicensePage().doNotAcceptTheAgreement();
//        installer.onSetup().clickNext();
//        installer.onDialog().clickNo();

        installer.onLicensePage().acceptTheAgreement();
        installer.onSetup().clickNext();
         
         installer.onDialog()
                     .clickNo();
         installer.onSetup().clickNext();
         
         installer.onLicensePage().acceptTheAggreement();
         installer.onSetup().clickNext();
         
         installer.onInstallationTypePage()
                     .chooseEasyInstall()
                     .chooseAdvancedInstall();
         
         installer.onSetup().clickNext();

         installer.onSelectComponentsPage()
                    .checkLibreOffice()
                    .checkJava().checkPostgreSQL();
         
         installer.onSetup().clickCancel();
         installer.onDialog().clickYes();
         
         installer.close();
    }
    
    @Test(groups={"demo"})
    public void testInstallerInWindows() throws Exception
    {
        //installer.open();
        //installer.waitForInstallerToOpen().clickOK();        
        installer.onSetup().clickNext();
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
    }
}