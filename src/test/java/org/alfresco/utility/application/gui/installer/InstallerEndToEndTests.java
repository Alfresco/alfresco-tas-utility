package org.alfresco.utility.application.gui.installer;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import static org.alfresco.utility.report.log.Step.STEP;

/**
 * Created by Claudia Agache on 7/10/2017.
 */
public class InstallerEndToEndTests extends InstallerTest
{
    @Autowired
    ACSUninstaller uninstaller;
    
    public void installationWithDefaultParameters()
    {
        //TODO
    }

    public void installationInAdvancedMode()
    {
        //TODO
    }

    public void verifyingComponentsVersion()
    {
        //TODO
    }


    @Test(groups={"demo"}, priority=0)

    public void uninstallAlfrescoOne() throws Exception {
        STEP("1. Run uninstall from Alfresco installation directory.");
        uninstaller.open();
        uninstaller.waitForUninstallerToOpen();
        STEP("2. At the 'Question' form click 'No' button.");
        uninstaller.onQuestionDialog().clickNo();
        uninstaller.assertDialogIsClosed();
        STEP("3. Run uninstaller again");
        uninstaller.open();
        uninstaller.waitForUninstallerToOpen();
        STEP("4. At the 'Question' form click 'Yes' button.");
        uninstaller.onQuestionDialog().clickYes();
        uninstaller.waitForConfirmationDialog();
        STEP("5. When uninstalling completed check Alfresco installation folder.");
        uninstaller.assertInstallationFolderIsEmpty();
    }
}
