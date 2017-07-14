package org.alfresco.utility.application.gui.installer;

import java.io.File;
import java.nio.file.Paths;

import org.alfresco.utility.application.gui.AbstractGuiTest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by Claudia Agache on 7/10/2017.
 */
@ContextConfiguration("classpath:alfresco-tester-context.xml")
public abstract class InstallerTest extends AbstractGuiTest
{
    @Autowired ACSInstaller installer;

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

    public void navigateToDatabaseServerParametersPage() throws Exception
    {
        navigateToInstallationTypeForm();
        installer.onInstallationTypePage().chooseAdvancedInstall();
        installer.onSetup().clickNext()
                .clickNext()
                .clickNext();
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
        installer.onSetup().clickNext().clickNext().clickNext();
    }

    public void navigateToLibreOfficeServerPortPage() throws Exception
    {
        navigateToTomcatPortConfigurationPage();
        installer.onSetup().clickNext();
    }

    public void navigateToFtpPortPage() throws Exception
    {
        navigateToLibreOfficeServerPortPage();
        installer.onSetup().clickNext().clickNext();
    }

    public void navigateToRmiPortPage() throws Exception
    {
        navigateToFtpPortPage();
        installer.onSetup().clickNext();
    }

    public void navigateToAdminPasswordForm() throws Exception
    {
        navigateToInstallationFolderForm();
        installer.onSetup().clickNext();
    }

}
