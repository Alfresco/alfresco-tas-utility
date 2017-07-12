package org.alfresco.utility.application.gui.installer;
import org.alfresco.utility.Utility;
import org.alfresco.utility.application.Focusable;
import org.alfresco.utility.application.gui.GuiScreen;
import org.alfresco.utility.exception.CouldNotFindApplicationActionImage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;
import org.springframework.stereotype.Component;
import org.testng.Assert;
import java.io.File;

@Component
public class ACSUninstaller extends ACSWizard
{
    public ACSWizard open() throws Exception {
        if (SystemUtils.IS_OS_WINDOWS) {
            Utility.executeOnWin(String.format("%s\\%s", installerProperties.getInstallerDestinationPath().getPath(), installerProperties.getOSProperty("uninstaller.name")));
            Thread.sleep(15000);
        } else {
            if (SystemUtils.IS_OS_LINUX)
                Utility.executeOnUnix(String.format("./%s", installerProperties.getInstallerDestinationPath().getPath(), installerProperties.getOSProperty("uninstaller.name")));
            throw new Exception("add supported code for this method");
        }

        return this;
    }

    public ACSUninstaller.QuestionDialog onQuestionDialog() throws Exception {
        return new QuestionDialog();
    }

    public ACSUninstaller.ConfirmationDialog onConfirmationDialog() throws Exception {
        return new ConfirmationDialog();
    }

    public ACSUninstaller.Setup onSetup() throws Exception {
        return new Setup();
    }

    public void assertDialogIsClosed() throws CouldNotFindApplicationActionImage {
        Assert.assertFalse(isPopUpDisplayed("dialog"));
    }

    public void assertInstallationFolderIsEmpty() {
        File[] list = installerProperties.getInstallerDestinationPath().listFiles();
        for (File item : list) {
            LOG.info(String.format("Found file/folder: %s", item.getPath()));
        }
        Assert.assertEquals(FileUtils.sizeOf(installerProperties.getInstallerDestinationPath()), 0, "Size of install folder(bytes): ");
    }

    @SuppressWarnings("unchecked")
    public ACSUninstaller.QuestionDialog waitForUninstallerToOpen() throws Exception {
        return new QuestionDialog();
    }

    public ACSUninstaller.ConfirmationDialog waitForConfirmationDialog() throws Exception {
        return new ConfirmationDialog();
    }

    @Override
    public GuiScreen focus() throws Exception {
        return null;
    }

    /*
        Uninstall - first dialog
     */
    public class QuestionDialog implements Focusable<ACSUninstaller.QuestionDialog> {
        public QuestionDialog() throws Exception {
            waitOn("dialog");
        }

        public void clickNo() throws Exception {
            clickOn("title");
            clickOn("no");
        }

        public void clickYes() throws Exception {
            clickOn("title");
            clickOn("yes");
        }

        @Override
        public QuestionDialog focus() throws Exception {
            clickOn("title");
            return this;
        }
    }

    /*
    Second dialog - Setup
     */
    public class Setup implements Focusable<Setup> {

        @Override
        public Setup focus() throws Exception {
            clickOn("setup/setup");
            return this;
        }
    }

    /*
    Third dialog - confirmation popup
     */

    public class ConfirmationDialog implements Focusable<ConfirmationDialog> {

        @Override
        public ConfirmationDialog focus() throws Exception {
            return null;
        }

        public ConfirmationDialog() throws Exception {
            waitOn("setup/confirmation");
        }
    }
}
