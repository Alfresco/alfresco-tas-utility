package org.alfresco.utility.application.gui.installer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import java.io.File;
import java.nio.file.Paths;

/**
 * Created by Claudia Agache on 7/10/2017.
 */
@ContextConfiguration("classpath:alfresco-tester-context.xml")
public abstract class InstallerTest extends AbstractTestNGSpringContextTests
{
    @Autowired
    ACSInstaller installer;

    public File createNotEmptyFolderInOS() throws Exception
    {
        File f = (SystemUtils.IS_OS_WINDOWS) ? Paths.get("C:\\tmp\\notEmptyFolder\\subDir").toFile() : Paths.get("/tmp/notEmptyFolder/subDir").toFile();
        if (!f.exists())
        {
            FileUtils.forceMkdir(f);
        }
        return f;
    }

    public void navigateToLanguageForm() throws Exception
    {
        installer.open();
        installer.waitForInstallerToOpen();
    }

    public void navigateToSetupForm() throws Exception
    {
        navigateToLanguageForm();
        installer.onLanguageSelectionDialog().clickOK();
    }

    public void navigateToLicenseForm() throws Exception
    {
        navigateToSetupForm();
        installer.onSetup().clickNext();
    }

    public void navigateToInstallationTypeForm() throws Exception
    {
        navigateToLicenseForm();
        installer.onLicensePage().acceptTheAgreement();
        installer.onSetup().clickNext();
    }

    public void navigateToInstallationFolderForm() throws Exception
    {
        navigateToInstallationTypeForm();
        installer.onInstallationTypePage().chooseEasyInstall();
        installer.onSetup().clickNext();
    }

    public void navigateToSelectComponentsForm() throws Exception
    {
        navigateToInstallationTypeForm();
        installer.onInstallationTypePage().chooseAdvancedInstall();
        installer.onSetup().clickNext();
    }

    public void navigateToTomcatPortConfigurationPage() throws Exception
    {
        navigateToSelectComponentsForm();
        installer.onSetup().clickNext()
                .clickNext()
                .clickNext();
    }

    public void navigateToLibreOfficeServerPortPage() throws Exception
    {
        navigateToTomcatPortConfigurationPage();
        installer.onSetup().clickNext();
    }

    public void navigateToFtpPortPage() throws Exception
    {
        navigateToLibreOfficeServerPortPage();
        installer.onSetup().clickNext()
                .clickNext();
    }

    public void navigateToRmiPortPage() throws Exception
    {
        navigateToFtpPortPage();
        installer.onSetup().clickNext();
    }

}
