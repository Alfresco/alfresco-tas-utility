package org.alfresco.utility.application.gui;

import java.io.File;

import org.alfresco.utility.Utility;
import org.alfresco.utility.application.Applicationable;
import org.alfresco.utility.application.Focusable;
import org.alfresco.utility.exception.CouldNotFindImageOnScreen;
import org.alfresco.utility.exception.TestConfigurationException;
import org.alfresco.utility.report.log.Step;
import org.apache.commons.lang.SystemUtils;
import org.sikuli.api.robot.Key;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;

/**
 * Inherit this class if you are dealing with GUI based application
 * 
 * @author Paul Brodner
 */
public abstract class GuiScreen extends Screen implements Applicationable, Focusable<GuiScreen>
{
    static final Screen screenHelperInstance = new Screen();

    public static Screen getScreenHelper()
    {
        return screenHelperInstance;
    }

    public static final int WAIT_TIMEOUT = 5;

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
     * @throws TestConfigurationException
     * @throws CouldNotFindImageOnScreen
     * @return the full string path of the image
     */
    protected String getImageActionRelatedToApp(String action) throws Exception
    {
        String os = "unix";

        if (SystemUtils.IS_OS_MAC)
        {
            os = "mac";
        }
        else if (SystemUtils.IS_OS_WINDOWS)
        {
            os = "win";
        }

        String location = String.format("shared-resources/gui/%s/%s/%s.png", os, getAppName(), action);
        File imageFile = Utility.getTestResourceFile(location);
        if (!imageFile.exists())
            throw new CouldNotFindImageOnScreen(imageFile.getPath(), getAppName(), "Missing image in your local resource folder. ");
        return imageFile.getPath();
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
    public GuiScreen waitOn(String imageAction) throws FindFailed, Exception
    {
        waitOn(imageAction, WAIT_TIMEOUT);
        return this;
    }

    /**
     * Wait for the image passed to be visible on screen for the given number of seconds
     * Usage:
     * if class name is "WindowsExplorer" and we want to wait for title for 5 seconds we can use
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
        Step.STEP(String.format("Wait for: [%s]", imageAction));
        wait(getImageActionRelatedToApp(imageAction), timeout);
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
     * @throws CouldNotFindImageOnScreen
     */
    public GuiScreen clickOn(String imageAction, int targetOffsetX, int targetOffsetY) throws CouldNotFindImageOnScreen
    {
        Step.STEP(String.format("Click on: [%s] at position [%d, %d]", imageAction, targetOffsetX, targetOffsetY));
        String location = "";
        try
        {
            location = getImageActionRelatedToApp(imageAction);
            Pattern pImage = new Pattern(location).targetOffset(targetOffsetX,targetOffsetY);
            click(pImage);
        }
        catch (FindFailed e)
        {
            throw new CouldNotFindImageOnScreen(location, getAppName(), e.getMessage());
        }
        catch (Exception e)
        {
            throw new CouldNotFindImageOnScreen(location, getAppName(), e.getMessage());
        }
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
     * @throws CouldNotFindImageOnScreen
     */
    public GuiScreen clickOn(String imageAction) throws CouldNotFindImageOnScreen
    {
        Step.STEP(String.format("Click on: [%s]", imageAction));
        String location = "";
        try
        {
            location = getImageActionRelatedToApp(imageAction);
            click(location);
        }
        catch (FindFailed e)
        {
            throw new CouldNotFindImageOnScreen(location, getAppName(), e.getMessage());
        }
        catch (Exception e)
        {
            throw new CouldNotFindImageOnScreen(location, getAppName(), e.getMessage());
        }
        return this;
    }

    protected boolean isPopUpDisplayed(String imageLocation) throws CouldNotFindImageOnScreen
    {
        try
        {
            waitOn(imageLocation);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public GuiScreen clearAndType(String value) throws Exception
    {
        if (SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_LINUX)
        {
            type("a", Key.CMD);
            type(Key.DELETE);
        }
        else if (SystemUtils.IS_OS_WINDOWS)
        {
            type("a", Key.CTRL);
            type(Key.DELETE);
        }
        type(value);
        return this;
    }

    public GuiScreen copyToClipboard() throws Exception
    {
        if (SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_LINUX)
        {
            type("c", Key.CMD);
        }
        else if (SystemUtils.IS_OS_WINDOWS)
        {
            type("c", Key.CTRL);
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
     * @throws CouldNotFindImageOnScreen
     */
    public GuiScreen checkOn(String imageAction) throws CouldNotFindImageOnScreen
    {
        Step.STEP(String.format("Check on: [%s]", imageAction));
        String location = "";
        try
        {
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
        }
        catch (Exception e)
        {
            throw new CouldNotFindImageOnScreen(location, getAppName(), e.getMessage());
        }

        return this;
    }

    /**
     * This will kill the application based on the process name defined
     */
    @Override
    public Applicationable killProcess() throws Exception
    {
        Utility.killProcessName(getProcessName());
        return null;
    }

    /**
     * Check if the process is running by process name defined
     */
    @Override
    public boolean isRunning()
    {
        return Utility.isProcessRunning(getProcessName());
    }
}
