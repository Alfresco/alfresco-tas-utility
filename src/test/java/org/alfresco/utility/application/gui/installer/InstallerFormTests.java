package org.alfresco.utility.application.gui.installer;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.alfresco.utility.report.log.Step.STEP;

/**
 * Created by Claudia Agache on 7/10/2017.
 */
public class InstallerFormTests extends InstallerTest
{
    public void languageSelectionForm() throws Exception
    {
        //TODO
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
    
    public void installationFolderForm() throws Exception
    {
        //TODO
    }

    public void selectComponentsForm() throws Exception
    {
        //TODO
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
