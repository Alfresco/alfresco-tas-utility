package org.alfresco.utility.application.gui.installer;

import org.alfresco.utility.application.gui.AbstractGuiTest;
import org.alfresco.utility.report.HtmlReportListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@ContextConfiguration("classpath:alfresco-tester-context.xml")
@Listeners(value = HtmlReportListener.class)
public class ACSEasyInstallTest extends AbstractGuiTest
{
    @Autowired
    ACSInstaller installer;

    @Test(groups = { "demo" })
    public void openAlfrescoInstallerWizard() throws Exception
    {
        installer.open();
        installer.waitForInstallerToOpen();
        installer.onLanguageSelectionDialog().clickOK();
        installer.onSetup().clickNext();
        installer.onLicensePage().acceptTheAgreement();
        installer.onSetup().clickNext();
        installer.onInstallationTypePage().chooseEasyInstall();
        installer.onSetup().clickNext();

        installer.onSetup().clickCancel();
        installer.onDialog().clickYes();
    }

}