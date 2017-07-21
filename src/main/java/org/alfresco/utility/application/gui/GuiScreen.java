package org.alfresco.utility.application.gui;

import java.io.File;
import java.io.IOException;

import org.alfresco.utility.Utility;
import org.alfresco.utility.application.Application;
import org.alfresco.utility.application.Focusable;
import org.alfresco.utility.exception.TestConfigurationException;
import org.alfresco.utility.report.log.Step;
import org.apache.commons.lang.SystemUtils;
import org.sikuli.api.robot.Key;
import org.sikuli.api.robot.KeyModifier;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;

/**
 * Inherit this class if you are dealing with GUI based application
 *
 * @author Paul Brodner
 */
public abstract class GuiScreen extends Screen implements Application, Focusable<GuiScreen>
{
    static final Screen screenHelperInstance = new Screen();

    public static Screen getScreenHelper()
    {
        return screenHelperInstance;
    }

    private String osName = "unknown";

    public static final int WAIT_TIMEOUT = 5;
    public static final int TEN_MINUTES = 600;
    public static final int FIVE_MINUTES = 300;

    /**
     * I assume the name of the app will be the name of the class.
     * We will look under share-resources/gui/<os-type>/<application-name> for all information
     *
     * @return
     */
    public String getAppName()
    {
        return this.getClass().getSimpleName().toLowerCase();
    }

    /**
     * Return the location of the "action" image from the "shared-resource/gui/<os-type>/<application-name>/<action>" folder
     *
     * @param action -> the the actual image found in the image resource folder. Add it without the "png" extention.
     * @return the full string path of the image
     * @throws TestConfigurationException
     * @throws Exception
     */
    protected String getImageActionRelatedToApp(String action) throws Exception
    {
        String osName = getOSName();

        String location = String.format("shared-resources/gui/%s/%s/%s.png", osName, getAppName(), action);
        File imageFile = Utility.getTestResourceFile(location);
        if (!imageFile.exists())
            throw new TestConfigurationException("missing image from your local resource folder: [ " + imageFile.getPath() + " ]");
        return imageFile.getPath();
    }

    public String getOSName() throws IOException
    {
        if(osName=="unknown")
        {
            osName= Utility.getOSName();
        }
        return osName;
    }

    /**
     * Wait until the image passed is visible on screen
     * Usage:
     * if class name is "WindowsExplorer" and we want to wait for title we can use
     * <code>
     * waitOn("title")
     * <code>
     * This means that we need to create this hierarchy:
     * "shared-resources/gui/win/windowsexplorer/title.png"
     *
     * @return {@link GuiScreen}
     * @throws FindFailed
     * @throws Exception
     */
    protected GuiScreen waitOn(String imageAction) throws FindFailed, Exception
    {
        waitOn(imageAction, WAIT_TIMEOUT);
        return this;
    }

    /**
     * Wait up to given number of seconds for the image passed to be visible on screen
     * Usage:
     * if class name is "WindowsExplorer" and we want to wait for title to be visible within 5 seconds we can use
     * <code>
     * waitOn("title", 5)
     * <code>
     * This means that we need to create this hierarchy:
     * "shared-resources/gui/win/windowsexplorer/title.png"
     *
     * @return {@link GuiScreen}
     * @throws FindFailed
     * @throws Exception
     */
    public GuiScreen waitOn(String imageAction, double timeout) throws FindFailed, Exception
    {
        Step.STEP(String.format("Wait up to %4.0f seconds for: [%s]", timeout, imageAction));
        wait(getImageActionRelatedToApp(imageAction), timeout);
        return this;
    }
    
    /**
     * Wait the given number of seconds for the image passed to not be visible on screen
     * Usage:
     * if class name is "WindowsExplorer" and we want to wait for title to disappear within 5 seconds we can use
     * <code>
     * waitVanishOn("title", 5)
     * <code>
     * This means that we need to create this hierarchy:
     * "shared-resources/gui/win/windowsexplorer/title.png"
     *
     * @return {@link GuiScreen}
     * @throws FindFailed
     * @throws Exception
     */
    public GuiScreen waitVanishOn(String imageAction, double timeout) throws FindFailed, Exception
    {
        Step.STEP(String.format("Wait up to %4.0f seconds for: [%s] to disappear", timeout, imageAction));
        waitVanish(getImageActionRelatedToApp(imageAction), timeout);
        return this;
    }

    /**
     * Click on the image passed if it's visible on screen at specified target offset
     * Usage:
     * if class name is "WindowsExplorer" and we want to click on "close" image
     * <code>
     * clickOn("close", 1, 1)
     * <code>
     * This means that we need to create this hierarchy:
     * "shared-resources/gui/win/windowsexplorer/close.png"
     * 
     * @param imageAction
     * @return
     * @throws TestConfigurationException 
     * @throws FindFailed 
     * @throws Exception
     */
    public GuiScreen clickOn(String imageAction, int targetOffsetX, int targetOffsetY) throws Exception
    {
        Step.STEP(String.format("Click on: [%s] at position [%d, %d]", imageAction, targetOffsetX, targetOffsetY));
        String location = "";

        location = getImageActionRelatedToApp(imageAction);
        Pattern pImage = new Pattern(location).targetOffset(targetOffsetX, targetOffsetY);
        click(pImage);
        return this;
    }

    /**
     * Click on the image passed if it's visible on screen
     * Usage:
     * if class name is "WindowsExplorer" and we want to click on "close" image
     * <code>
     * clickOn("close")
     * <code>
     * This means that we need to create this hierarchy:
     * "shared-resources/gui/win/windowsexplorer/close.png"
     *
     * @param imageAction
     * @return
     * @throws TestConfigurationException 
     * @throws FindFailed 
     * @throws Exception
     */
    public GuiScreen clickOn(String imageAction) throws Exception
    {
        Step.STEP(String.format("Click on: [%s]", imageAction));
        String location = "";

        location = getImageActionRelatedToApp(imageAction);
        click(location);

        return this;
    }

    protected boolean isPopUpDisplayed(String imageLocation) throws Exception
    {
        waitOn(imageLocation);
        return true;
    }

    public GuiScreen clearAndType(String value) throws Exception
    {
        if (SystemUtils.IS_OS_MAC)
        {
            type("a", Key.CMD);
            type(Key.DELETE);
        }
        else if (SystemUtils.IS_OS_WINDOWS || SystemUtils.IS_OS_LINUX)
        {
            type("a", Key.CTRL);
            type(Key.DELETE);
        }
        type(value);
        return this;
    }

    public GuiScreen copyToClipboard() throws Exception
    {
        if (SystemUtils.IS_OS_MAC)
        {
            type("a", Key.CMD);
            type("c", Key.CMD);
        }
        else if (SystemUtils.IS_OS_WINDOWS)
        {
            type("a", Key.CTRL);
            type("c", Key.CTRL);
        }
        else if (SystemUtils.IS_OS_LINUX)
        {
            type("a", KeyModifier.CTRL);
            type("c", KeyModifier.CTRL);
        }
        return this;
    }

    /**
     * This will check one checkbox, selecting the image passed as argument then press on keyboard SPACE
     * Usage:
     * if class name is "WindowsExplorer" and we want to check checkbox identified by image "folderA"
     * <code>
     * checkOn("folderA")
     * <code>
     * This means that we need to create this hierarchy:
     * "shared-resources/gui/win/windowsexplorer/folderA.png"
     *
     * @param imageAction
     * @return
     * @throws TestConfigurationException 
     * @throws Exception 
     * @throws Exception
     */
    public GuiScreen checkOn(String imageAction) throws Exception
    {
        Step.STEP(String.format("Check on: [%s]", imageAction));
        String location = "";

        location = getImageActionRelatedToApp(imageAction);
        if (SystemUtils.IS_OS_LINUX)
        {
            type(location, Key.LEFT);
            type(Key.ENTER);
        }
        else
        {
            type(location, Key.SPACE);
        }

        return this;

    }
}
