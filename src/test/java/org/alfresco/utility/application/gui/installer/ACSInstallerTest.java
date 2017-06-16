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
         installer.open();
         installer.waitForInstallerToOpen()
                  .clickOK()
                  .clickCancel();
         
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
    }
}
