package org.alfresco.utility.application.gui.installer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.alfresco.utility.report.log.Step.STEP;

import org.alfresco.utility.application.gui.installer.ACSInstallerProperties.DESCRIPTION;
import org.alfresco.utility.application.gui.installer.ACSInstallerProperties.LANGUAGES;

/**
 * Created by Claudia Agache on 7/10/2017.
 */
public class InstallerFormTests extends InstallerTest
{

    /**
     * AONE-18286
     */
    @Test public void languageSelectionForm() throws Exception
    {
        STEP("Precondition: Alfresco One installer is started and navigated to Language Selection form");
        navigateToLanguageForm();

        STEP("1. Press Cancel button and check that installer is closed.");
        installer.onLanguageSelectionDialog().clickCancel();
        Assert.assertFalse(installer.isRunning(), "The installer should be closed.");

        STEP("2. Start installer again and check that Language Selection form is displayed");
        navigateToLanguageForm();
        installer.onLanguageSelectionDialog().focus();

        STEP("3. Select a language different than English and press OK");
        installer.onLanguageSelectionDialog().setLanguage(LANGUAGES.FRENCH).startWithFrenchSetup();
        installer.onFrenchSetup().assertDescriptionIs(DESCRIPTION.FRENCH);
    }

    /**
     * AONE-18287
     */
    @Test public void welcomeForm() throws Exception
    {
        STEP("Precondition: Alfresco One installer is started and navigated to Setup/Welcome form");
        navigateToSetupForm();

        STEP("1. Go to Setup page, press Back button then assert that it is disabled");
        installer.onSetup().clickBack().assertBackButtonIsDisabled();

        STEP("2. Press Cancel button. Warning 'Do you want to abort the installation process?' form is displayed.");
        installer.onSetup().clickCancel();

        STEP("3. Press No button. Question' form closed, installer is still running.");
        installer.onDialog().clickNo();
        installer.onSetup().focus();
        Assert.assertTrue(installer.isRunning(), "The installer should not be closed.");

        STEP("4. Press Cancel button. Warning 'Do you want to abort the installation process?' form is displayed.");
        installer.onSetup().clickCancel();

        STEP("5. Press Yes button. 'Question' form closed, installer closed, installation process aborted.");
        installer.onDialog().clickYes();
        Assert.assertFalse(installer.isRunning(), "The installer should be closed.");

        STEP("6. Run installer again, open Setup, press Next and check License Agreement page is opened.");
        navigateToSetupForm();
        installer.onSetup().clickNext();
        installer.onLicensePage().focus().noOptionIsSelected();
    }

    /**
     * AONE-18457
     */
    @Test() public void licenseAgreementForm() throws Exception
    {
        STEP("Precondition: Alfresco One installer is started and navigated to License Agreement form");
        navigateToLicenseForm();

        STEP("1. Scroll the Alfresco Enterprise Trial Agreement text.");
        installer.onLicensePage().scrollAgreement();

        STEP("2. Don't select any of the two options and press Next button.");
        installer.onLicensePage().noOptionIsSelected();
        installer.onSetup().clickNext();
        installer.onLicensePage();

        STEP("3. Select 'I do not accept the agreement' options and press Next button.");
        installer.onLicensePage().doNotAcceptTheAgreement();
        installer.onSetup().clickNext();

        STEP("4. From the Abort installation process window, select Yes.");
        installer.onDialog().clickYes();
        Assert.assertFalse(installer.isRunning(), "The installer should be closed and the installation process is aborted.");

        STEP("5. Run installer again and navigate to the License Agreement form. Select 'I do not accept the agreement' and press the Next button.");
        navigateToLicenseForm();
        installer.onLicensePage().doNotAcceptTheAgreement();
        installer.onSetup().clickNext();

        STEP("6. From the Abort installation process window, select No.");
        installer.onDialog().clickNo();
        installer.onLicensePage().noOptionIsSelected();

        STEP("7. Press Cancel button.");
        installer.onSetup().clickCancel();

        STEP("8. From the Abort installation process window, select Yes.");
        installer.onDialog().clickYes();
        Assert.assertFalse(installer.isRunning(), "The installer should be closed and the installation process is aborted.");

        STEP("9. Run installer again and navigate to the License Agreement form. Press Cancel and from the abort installation windows press 'No'.");
        navigateToLicenseForm();
        installer.onSetup().clickCancel();
        installer.onDialog().clickNo();
        installer.onLicensePage().noOptionIsSelected();

        STEP("10. Press Back button.");
        installer.onSetup().clickBack().checkSetupDescription();

        STEP("11. From the Setup - Alfresco One press Next button.");
        installer.onSetup().clickNext();

        STEP("12.  Select the 'I accept the agreement' option and press Next.");
        installer.onLicensePage().acceptTheAgreement();
        installer.onSetup().clickNext();
        installer.onInstallationTypePage();
        installer.close();
    }

    /**
     * AONE-18288
     */
    @Test() public void installationTypeForm() throws Exception
    {
        STEP("Precondition: Alfresco One installer is started and navigated to Installation Type form");
        navigateToInstallationTypeForm();

        STEP("1. The 'Easy - Install using the default configuration' radio button is selected. Press Next button.");
        installer.onSetup().clickNext();
        installer.onInstallationFolderPage();

        STEP("2. Click 'Back' button, then select radio button 'Advanced -configure server ports and service properties' and click on 'Next' button");
        installer.onSetup().clickBack();
        installer.onInstallationTypePage().chooseAdvancedInstall();
        installer.onSetup().clickNext();
        installer.onSelectComponentsPage();

        STEP("3. Click 'Back' and on 'Installation Type' form click on 'Cancel' button");
        installer.onSetup().clickBack();
        installer.onInstallationTypePage();
        installer.onSetup().clickCancel();

        STEP("4. From the Abort installation process window, select No.");
        installer.onDialog().clickNo();
        installer.onInstallationTypePage();

        STEP("8. Click on 'Cancel'  button again and this time select 'Yes' as answer for the Question.");
        installer.onSetup().clickCancel();
        installer.onDialog().clickYes();
        Assert.assertFalse(installer.isRunning(), "The installer should be closed and the installation process is aborted.");
    }

    /**
     * AONE-18290
     */
    @Test() public void installationFolderForm() throws Exception
    {
        STEP("Precondition: Alfresco One installer is running in easy install mode - Installation Folder form is opened.");
        navigateToInstallationFolderForm();

        STEP("1. Select a non-empty folder (e.g. /opt/temp) and click 'Next' button.");
        String notEmptyFolder = createNotEmptyFolderInOS().getParent();
        installer.onInstallationFolderPage().setDestination(notEmptyFolder);
        installer.onSetup().clickNext();
        Assert.assertTrue(installer.onInstallationFolderPage().isSelectedFolderNotEmptyWarningDisplayed(),
                "'The selected folder is not empty. Specify a different folder' warning is not displayed.");
        installer.onWarning().clickOK();

        STEP("2. Enter any random string to the 'Select a folder' field, click 'Forward' button and then click 'Back' button.");
        installer.onInstallationFolderPage().setDestination("randomDir");
        installer.onSetup().clickNext();
        installer.onAdminPasswordPage();
        installer.onSetup().clickBack();

        //The path from where Alfresco One is launched is added automatically ahead of entered text.
        String parent = installer.getFileProperties().getInstallerSourcePath().getParent();
        String destinationFolder = String.format("%s\\randomDir", parent);
        installer.onInstallationFolderPage().assertInstallationFolderIs(destinationFolder);

        STEP("3. Select empty folder and click 'Forward' button.");
        destinationFolder = installer.getFileProperties().getInstallerDestinationPath().getPath();
        installer.onInstallationFolderPage().setDestination(destinationFolder);
        installer.onSetup().clickNext();

        STEP("4. Click 'Back' button until the 1st step will not be reached and then return to 'Installation Folder' form.");
        installer.onSetup().clickBack().clickBack().clickNext();
        installer.onInstallationFolderPage().assertInstallationFolderIs(destinationFolder);

        STEP("5. Click 'Cancel' button ");
        installer.onSetup().clickCancel();

        STEP("6. Click 'Yes' button and verify <install dir>.");
        installer.onDialog().clickYes();
        Assert.assertFalse(installer.isRunning(), "The installer should be closed and the installation process is aborted.");
        installer.assertInstallationFolderIsEmpty();
    }

    //    @Test()
    public void selectComponentsForm() throws Exception
    {
        STEP("Precondition: Alfresco One installer is running in advanced install mode - Select Components form is opened.");
        navigateToSelectComponentsForm();
    }

    /*
    AONE-18296
     */
    @Test public void adminPasswordForm() throws Exception
    {
        STEP("Precondition: Navigate to Admin Password form");
        navigateToAdminPasswordForm();
        STEP("1. Enter different admin password and repeat password values. Click 'Next' button.");
        installer.onAdminPasswordPage().setAdminPassword("Pass");
        installer.onAdminPasswordPage().setRepeatAdminPassword("Passwrd123");
        installer.onSetup().clickNext();
        Assert.assertTrue(installer.onAdminPasswordPage().isPasswordsDoNotMatchWarningDisplayed(), "Passwords do not match warning message is not displayed.");
        installer.onWarning().clickOK();
        STEP("2. Enter equal password admin password and repeat password, but shorter than 3 symbols and click 'Next' button.");
        installer.onAdminPasswordPage().setAdminPassword("ab");
        installer.onAdminPasswordPage().setRepeatAdminPassword("ab");
        installer.onSetup().clickNext();
        Assert.assertTrue(installer.onAdminPasswordPage().isPasswordToShortWarningDisplayed(),
                "Passwords should be longer than 3 characters warning is not displayed.");
        installer.onWarning().clickOK();
        STEP("3. Enter equal password admin password and password confirmation. Click 'Forward' button ");
        String validPassword = "adminP1";
        installer.onAdminPasswordPage().setAdminPassword(validPassword);
        installer.onAdminPasswordPage().setRepeatAdminPassword(validPassword);
        installer.onSetup().clickNext();
        Assert.assertTrue(installer.onAdminPasswordPage().isReadyToInstallFormDisplayed(), "The next form, 'Ready to install' is not displayed");
        STEP("4. Click on 'Back' button until 1st page opened. Then return to this page.");
        installer.onSetup().clickBack();
        installer.onSetup().clickBack();
        installer.onSetup().clickBack();
        installer.onSetup().clickBack();
        installer.onSetup().clickBack();
        installer.onSetup().clickNext();
        installer.onLicensePage().acceptTheAgreement();
        installer.onSetup().clickNext();
        installer.onSetup().clickNext();
        installer.onSetup().clickNext();
        Assert.assertTrue(installer.onAdminPasswordPage().isPreviouslyProvidedPasswordAvailable(), "The previousley provided password is no longer available");
        STEP("5. Click on 'Cancel' button.Verify <install dir>.");
        installer.onSetup().clickCancel();
        installer.onWarning().clickOK();
        installer.assertInstallationFolderIsEmpty();
        //TODO check this 2 step in an end to end test that installs alfresco
        STEP("7. Run installer again, specify correct and equal passwords for admin and install Alfresco.");
        STEP("8. When Alfresco is installed try to login to Alfresco One Share page as admin using password you set during install.");
    }

    public void databaseServerParametersForm() throws Exception
    {
        //TODO
    }

    @Test() public void tomcatPortConfigurationForm() throws Exception
    {
        STEP("Precondition: Alfresco One installer is started and navigated to Tomcat Port Configuration form");
        navigateToTomcatPortConfigurationPage();

        STEP("1. For Tomcat Server Port enter incorrect port values instead of default values used by default. For example, 66000. Click Forward.");
        installer.onTomcatPortConfigurationPage().setTomcatServerPort("66000");
        installer.onSetup().clickNext();
        Assert.assertTrue(installer.onTomcatPortConfigurationPage().isTomcatServerPortWarningMessageDisplayed(),
                "Select another LibreOffice Server Port message should be displayed.");
        installer.onWarning().clickOK();

        STEP("2. Enter incorrect symbolic port value, DF, for example. Click Forward");
        installer.onTomcatPortConfigurationPage().setTomcatServerPort("DF");
        installer.onSetup().clickNext();
        Assert.assertTrue(installer.onTomcatPortConfigurationPage().isTomcatPortCharacterWarningMessageDisplayed(),
                "Use numbers only in the port configuration field message should be displayed.");
        installer.onWarning().clickOK();

        STEP("3. Enter correct port value, not used by another process (use netstat console utility to define of using ports). Click Forward.");
        installer.onTomcatPortConfigurationPage().setTomcatServerPort();
        installer.onSetup().clickNext();
        installer.onLibreOfficeServerPortPage().focus();
        installer.onSetup().clickBack();

        STEP("4. For Tomcat Shutdown Port enter incorrect port values instead of default values used by default. For example, 66000. Click Forward.");
        installer.onTomcatPortConfigurationPage().setTomcatShutdownPort("66000");
        installer.onSetup().clickNext();
        Assert.assertTrue(installer.onTomcatPortConfigurationPage().isTomcatShutdownPortWarningMessageDisplayed(),
                "Select another LibreOffice Server Port message should be displayed.");
        installer.onWarning().clickOK();

        STEP("5. Enter incorrect symbolic port value, DF, for example. Click Forward");
        installer.onTomcatPortConfigurationPage().setTomcatShutdownPort("DF");
        installer.onSetup().clickNext();
        Assert.assertTrue(installer.onTomcatPortConfigurationPage().isTomcatPortCharacterWarningMessageDisplayed(),
                "Use numbers only in the port configuration field message should be displayed.");
        installer.onWarning().clickOK();

        STEP("6. Enter correct port value, not used by another process (use netstat console utility to define of using ports). Click Forward.");
        installer.onTomcatPortConfigurationPage().setTomcatShutdownPort();
        installer.onSetup().clickNext();
        installer.onLibreOfficeServerPortPage().focus();
        installer.onSetup().clickBack();

        STEP("7. For Tomcat SSL Port enter incorrect port values instead of default values used by default. For example, 66000. Click Forward.");
        installer.onTomcatPortConfigurationPage().setTomcatSSLPort("66000");
        installer.onSetup().clickNext();
        Assert.assertTrue(installer.onTomcatPortConfigurationPage().isTomcatSSLPortWarningMessageDisplayed(),
                "Select another LibreOffice Server Port message should be displayed.");
        installer.onWarning().clickOK();

        STEP("8. Enter incorrect symbolic port value, DF, for example. Click Forward");
        installer.onTomcatPortConfigurationPage().setTomcatSSLPort("DF");
        installer.onSetup().clickNext();
        Assert.assertTrue(installer.onTomcatPortConfigurationPage().isTomcatPortCharacterWarningMessageDisplayed(),
                "Use numbers only in the port configuration field message should be displayed.");
        installer.onWarning().clickOK();

        STEP("9. Enter correct port value, not used by another process (use netstat console utility to define of using ports). Click Forward.");
        installer.onTomcatPortConfigurationPage().setTomcatSSLPort();
        installer.onSetup().clickNext();
        installer.onLibreOfficeServerPortPage().focus();
        installer.onSetup().clickBack();

        STEP("10. For Tomcat AJP Port enter incorrect port values instead of default values used by default. For example, 66000. Click Forward.");
        installer.onTomcatPortConfigurationPage().setTomcatAJPPort("66000");
        installer.onSetup().clickNext();
        Assert.assertTrue(installer.onTomcatPortConfigurationPage().isTomcatAJPPortWarningMessageDisplayed(),
                "Select another LibreOffice Server Port message should be displayed.");
        installer.onWarning().clickOK();

        STEP("11. Enter incorrect symbolic port value, DF, for example. Click Forward");
        installer.onTomcatPortConfigurationPage().setTomcatAJPPort("DF");
        installer.onSetup().clickNext();
        Assert.assertTrue(installer.onTomcatPortConfigurationPage().isTomcatPortCharacterWarningMessageDisplayed(),
                "Use numbers only in the port configuration field message should be displayed.");
        installer.onWarning().clickOK();

        STEP("12. Enter correct port value, not used by another process (use netstat console utility to define of using ports). Click Forward.");
        installer.onTomcatPortConfigurationPage().setTomcatAJPPort();
        installer.onSetup().clickNext();
        installer.onLibreOfficeServerPortPage().focus();

        STEP("13. Click 'Cancel' button and select 'Yes'");
        installer.onSetup().clickCancel();
        installer.onDialog().clickYes();
        Assert.assertFalse(installer.isRunning(), "The installer should be closed and the installation process is aborted.");
    }

    /**
     * AONE-18296 + AONE-18295 + AONE-18293
     */
    @Test() public void verifyLibreOfficeFtpPortRmiPortForms() throws Exception
    {
        STEP("Precondition: Alfresco One installer is started and navigated to LibreOffice Server Port form");
        navigateToLibreOfficeServerPortPage();

        STEP("1. To the 'LibreOffice Server port' enter any busy port (or leave busy 8100 port) and verify error message is displayed.");
        installer.onLibreOfficeServerPortPage().setLibreOfficeServerPort("66000");
        installer.onSetup().clickNext();
        Assert.assertTrue(installer.onLibreOfficeServerPortPage().isLibreOfficePortWarningMessageDisplayed(),
                "Select another LibreOffice Server Port message should be displayed.");
        installer.onWarning().clickOK();

        STEP("2. To the 'Alfresco FTP port' enter any incorrect data, e.g. text click 'Next' button.");
        installer.onLibreOfficeServerPortPage().setLibreOfficeServerPort("DF");
        installer.onSetup().clickNext();
        Assert.assertTrue(installer.onLibreOfficeServerPortPage().isLibreOfficeCharacterWarningMessageDisplayed(),
                "Use numbers only in the port configuration field message should be displayed.");
        installer.onWarning().clickOK();

        STEP("3. Enter correct port value, not used by another process (use netstat console utility to define of using ports). Click Forward.");
        installer.onLibreOfficeServerPortPage().setLibreOfficeServerPort();
        installer.onSetup().clickNext();

        STEP("4. To the 'Alfresco FTP Port' enter any busy port (or leave busy 21 port) and verify error message is displayed.");
        installer.onSetup().clickNext();
        installer.onFtpPortPage().setFtpPort("66000");
        installer.onSetup().clickNext();
        Assert.assertTrue(installer.onFtpPortPage().isFtpPortWarningMessageDisplayed(), "Select another Ftp Port message should be displayed.");
        installer.onWarning().clickOK();

        STEP("5. To the 'Alfresco FTP port' enter any incorrect data, e.g. text click 'Next' button.");
        installer.onFtpPortPage().setFtpPort("DF");
        installer.onSetup().clickNext();
        Assert.assertTrue(installer.onFtpPortPage().isFtpCharacterWarningMessageDisplayed(),
                "Use numbers only in the port configuration field message should be displayed.");
        installer.onWarning().clickOK();

        STEP("6. Enter correct port value, not used by another process (use netstat console utility to define of using ports). Click Forward.");
        installer.onFtpPortPage().setFtpPort("21");
        installer.onSetup().clickNext();

        STEP("7. To the 'Alfresco RMI Port' enter any busy port (or leave busy 50500 port) and verify error message is displayed.");
        installer.onRmiPortPage().setRmiPort("66000");
        installer.onSetup().clickNext();
        Assert.assertTrue(installer.onRmiPortPage().isRmiPortWarningMessageDisplayed(), "Select another RMI Port message should be displayed.");
        installer.onWarning().clickOK();

        STEP("8. To the 'Alfresco RMI port' enter any incorrect data, e.g. text click 'Next' button.");
        installer.onRmiPortPage().setRmiPort("DF");
        installer.onSetup().clickNext();
        Assert.assertTrue(installer.onRmiPortPage().isRmiCharacterWarningMessageDisplayed(),
                "Use numbers only in the port configuration field message should be displayed.");
        installer.onWarning().clickOK();

        STEP("9. Enter correct port value, not used by another process (use netstat console utility to define of using ports). Click Forward.");
        installer.onRmiPortPage().setRmiPort();
        installer.onSetup().clickNext();

        STEP("10. Install Alfresco.");
        installer.onAdminPasswordPage().setAdminPassword(installer.installerProperties.getAdminPassword());
        installer.onAdminPasswordPage().setRepeatAdminPassword(installer.installerProperties.getAdminPassword());
        installer.onSetup().clickNext();
        installer.onServiceStartupConfigurationPage().focus();
        installer.onSetup().clickNext();
        installer.onReadyToInstallPage().focus();
        installer.onSetup().clickNext();
        Assert.assertTrue(installer.isRunning(), "The installation process should be running.");
    }

    public void shardedSolrForm() throws Exception
    {
        //TODO
    }
}
