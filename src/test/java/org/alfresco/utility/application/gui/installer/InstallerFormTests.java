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
        installer.navigateToLicensePage();

        STEP("1. Scroll the Alfresco Enterprise Trial Agreement text.");
        installer.onLicensePage().scrollAgreement();

        STEP("2. Don't select any of the two options and press Next button.");
        installer.onSetup().clickNext();
        installer.onLicensePage().focus();

        STEP("3. Select 'I do not accept the agreement' options and press Next button.");
        installer.onLicensePage().doNotAcceptTheAgreement();
        installer.onSetup().clickNext();

        STEP("4. From the Abort installation process window, select Yes.");
        installer.onDialog().clickYes();
        Assert.assertFalse(installer.isProcessRunning(), "The installer should be closed and the installation process is aborted.");

        STEP("5. Run installer again and navigate to the License Agreement form. Select 'I do not accept the agreement' and press the Next button.");
        installer.navigateToLicensePage().doNotAcceptTheAgreement();
        installer.onSetup().clickNext();

        STEP("6. From the Abort installation process window, select No.");
        installer.onDialog().clickNo();
        installer.onLicensePage().noOptionIsSelected();

        STEP("7. Press Cancel button.");
        installer.onSetup().clickCancel();

        STEP("8. From the Abort installation process window, select Yes.");
        installer.onDialog().clickYes();
        Assert.assertFalse(installer.isProcessRunning(), "The installer should be closed and the installation process is aborted.");

        STEP("9. Run installer again and navigate to the License Agreement form. Press Cancel and from the abort installation windows press 'No'.");
        installer.navigateToLicensePage();
        installer.onSetup().clickCancel();
        installer.onDialog().clickNo();
        installer.onLicensePage().noOptionIsSelected();

        STEP("10. Press Back button.");

    }

    public void installationTypeForm() throws Exception
    {
        //TODO
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
