package org.alfresco.utility.application.gui.installer;

import org.alfresco.utility.Utility;
import org.alfresco.utility.application.Focusable;
import org.alfresco.utility.application.gui.GuiScreen;
import org.alfresco.utility.exception.CouldNotFindApplicationActionImage;
import org.apache.commons.lang.SystemUtils;
import org.springframework.stereotype.Component;
import org.testng.Assert;

@Component
public class ACSUninstaller extends ACSWizard
{
    public ACSWizard open() throws Exception
    {
        if (SystemUtils.IS_OS_WINDOWS)
        {
            Utility.executeOnWin(String.format("%s\\%s", installerProperties.getInstallerDestinationPath().getPath(),
                    installerProperties.getOSProperty("uninstaller.name")));
            Thread.sleep(15000);
        }
        else
        {
            if (SystemUtils.IS_OS_LINUX)
                Utility.executeOnUnix(String.format("./%s", installerProperties.getInstallerDestinationPath().getPath(),
                        installerProperties.getOSProperty("uninstaller.name")));
            throw new Exception("add supported code for this method");
        }

        return this;
    }

    public ACSUninstaller.QuestionDialog onQuestionDialog() throws Exception
    {
        return new QuestionDialog();
    }

    public ACSUninstaller.ConfirmationDialog onConfirmationDialog() throws Exception
    {
        return new ConfirmationDialog();
    }

    public ACSUninstaller.Setup onSetup() throws Exception
    {
        return new Setup();
    }

    public void assertDialogIsClosed() throws CouldNotFindApplicationActionImage
    {
        Assert.assertFalse(isPopUpDisplayed("dialog"));
    }

    @SuppressWarnings("unchecked")
    public ACSUninstaller.QuestionDialog waitForUninstallerToOpen() throws Exception
    {
        return new QuestionDialog();
    }

    public ACSUninstaller.ConfirmationDialog waitForConfirmationDialog() throws Exception
    {
        return new ConfirmationDialog();
    }

    @Override
    public GuiScreen focus() throws Exception
    {
        return null;
    }

    /*
     * Uninstall - first dialog
     */
    public class QuestionDialog implements Focusable<ACSUninstaller.QuestionDialog>
    {
        public QuestionDialog() throws Exception
        {
            waitOn("dialog");
        }

        public void clickNo() throws Exception
        {
            clickOn("title");
            clickOn("no");
        }

        public void clickYes() throws Exception
        {
            clickOn("title");
            clickOn("yes");
        }

        @Override
        public QuestionDialog focus() throws Exception
        {
            clickOn("title");
            return this;
        }
    }

    /*
     * Second dialog - Setup
     */
    public class Setup implements Focusable<Setup>
    {

        @Override
        public Setup focus() throws Exception
        {
            clickOn("setup/setup");
            return this;
        }
    }

    /*
     * Third dialog - confirmation popup
     */

    public class ConfirmationDialog implements Focusable<ConfirmationDialog>
    {

        public ConfirmationDialog() throws Exception
        {
            waitOn("setup/confirmation");
        }

        @Override
        public ConfirmationDialog focus() throws Exception
        {
            return null;
        }
    }
}
