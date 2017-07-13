package org.alfresco.utility.application.gui.installer;

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
	
	@Test
    public void languageSelectionForm() throws Exception
    {
        STEP("Precondition: Alfresco One installer is started and navigated to Language Selection form");
        installer.navigateToLanguageForm();
        
        STEP("1. Press Cancel button and check that installer is closed.");
        installer.onLanguageSelectionDialog().clickCancel();
        Assert.assertFalse(installer.isRunning(), "The installer should be closed.");
        
        STEP("2. Start installer again and check that Language Selection form is displayed");
        installer.navigateToLanguageForm().focus();
        
        STEP("3. Select a language different than English and press OK");
        installer.onLanguageSelectionDialog().setLanguage(LANGUAGES.FRENCH).clickOK();
        installer.onFrenchSetup().assertDescriptionIs(DESCRIPTION.FRENCH);
    }

    public void welcomeForm() throws Exception
    {
        //TODO
    }

    @Test()
    public void licenseAgreementForm() throws Exception
    {
        STEP("Precondition: Alfresco One installer is started and navigated to License Agreement form");
        installer.navigateToLicenseForm();

        STEP("1. Scroll the Alfresco Enterprise Trial Agreement text.");
        installer.onLicensePage().scrollAgreement();

        STEP("2. Don't select any of the two options and press Next button.");
        installer.onSetup().clickNext();
        installer.onLicensePage();

        STEP("3. Select 'I do not accept the agreement' options and press Next button.");
        installer.onLicensePage().doNotAcceptTheAgreement();
        installer.onSetup().clickNext();

        STEP("4. From the Abort installation process window, select Yes.");
        installer.onDialog().clickYes();
        Assert.assertFalse(installer.isRunning(), "The installer should be closed and the installation process is aborted.");

        STEP("5. Run installer again and navigate to the License Agreement form. Select 'I do not accept the agreement' and press the Next button.");
        installer.navigateToLicenseForm().doNotAcceptTheAgreement();
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
        installer.navigateToLicenseForm();
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

    @Test()
    public void installationTypeForm() throws Exception
    {
        STEP("Precondition: Alfresco One installer is started and navigated to Installation Type form");
        installer.navigateToInstallationTypeForm();

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

    @Test()
    public void installationFolderForm() throws Exception
    {
        STEP("Precondition: Alfresco One installer is running in easy install mode - Installation Folder form is opened.");
        installer.navigateToInstallationFolderForm();

        STEP("1. Select a non-empty folder (e.g. /opt/temp) and click 'Next' button.");
        String notEmptyFolder = createNotEmptyFolderInOS().getParent();
        installer.onInstallationFolderPage().setDestination(notEmptyFolder);
        installer.onSetup().clickNext();
        Assert.assertTrue(installer.onInstallationFolderPage().isSelectedFolderNotEmptyWarningDisplayed(), "'The selected folder is not empty. Specify a different folder' warning is not displayed.");
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
        installer.onSetup()
                .clickBack()
                .clickBack()
                .clickNext();
        installer.onInstallationFolderPage().assertInstallationFolderIs(destinationFolder);

        STEP("5. Click 'Cancel' button ");
        installer.onSetup().clickCancel();

        STEP("6. Click 'Yes' button and verify <install dir>.");
        installer.onDialog().clickYes();
        Assert.assertFalse(installer.isRunning(), "The installer should be closed and the installation process is aborted.");
    }

//    @Test()
    public void selectComponentsForm() throws Exception
    {
        STEP("Precondition: Alfresco One installer is running in advanced install mode - Select Components form is opened.");
        installer.navigateToSelectComponentsForm();
    }

    public void adminPasswordForm() throws Exception
    {
        //TODO
    }

    public void databaseServerParametersForm() throws Exception
    {
        //TODO
    }

    public void tomcatPortConfigurationForm() throws Exception
    {
        //TODO
    }

    public void LibreOfficeServerPortForm() throws Exception
    {
        //TODO
    }

    public void shardedSolrForm() throws Exception
    {
        //TODO
    }

    public void ftpPortForm() throws Exception
    {
        //TODO
    }
    public void rmiPortForm() throws Exception
    {
        //TODO
    }
}
