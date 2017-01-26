package org.alfresco.utility.network;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import org.alfresco.utility.LogFactory;
import org.alfresco.utility.Utility;
import org.alfresco.utility.data.DataValue;
import org.alfresco.utility.model.UserModel;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.testng.Assert;

/**
 * @author Paul Brodner
 */
public abstract class NetworkDrive
{
    static Logger LOG = LogFactory.getLogger();

    private String serverNetworkPath;
    private String localVolumePath;
    private UserModel userForNetworkAccess = new UserModel(DataValue.UNDEFINED.toString(), DataValue.UNDEFINED.toString());

    /**
     * For MAC {@link #mountOsApp} can be mount_smbfs
     * 
     * @param mountOsapp
     * @param networkPath
     * @param localVolumePath
     */
    public NetworkDrive(UserModel userModel, String serverNetworkPath, String localVolumePath)
    {
        setUserForNetworkAccess(userModel);
        setLocalVolumePath(localVolumePath);
        setServerNetworkPath(serverNetworkPath);
    }

    protected abstract void mountCode() throws Exception;

    protected abstract void umountCode() throws Exception;

    public void mount() throws Exception
    {
        LOG.info("Mounting {} using User {} to drive {} ", getServerNetworkPath(), getUserForNetworkAccess().toString(), getLocalVolumePath());
        mountCode();
        assertNetworkDriveIsMounted();
    }

    public void unount() throws Exception
    {
        LOG.info("UnMounting drive {} using User {}", getLocalVolumePath(), getUserForNetworkAccess().toString());
        umountCode();
    }

    public UserModel getUserForNetworkAccess()
    {
        return userForNetworkAccess;
    }

    public void setUserForNetworkAccess(UserModel userModel)
    {
        this.userForNetworkAccess = userModel;
    }

    protected void runCommand(String command, Object... arguments) throws Exception
    {
        if (SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC)
        {
            Utility.executeOnUnix(String.format(command, arguments));
        }
        else
        {
            Utility.executeOnWin(String.format(command, arguments));
        }
    }

    public String getLocalVolumePath()
    {
        return localVolumePath;
    }

    public void setLocalVolumePath(String localVolumePath)
    {
        this.localVolumePath = localVolumePath;
    }

    public String getServerNetworkPath()
    {
        return serverNetworkPath;
    }

    public void setServerNetworkPath(String serverNetworkPath)
    {
        this.serverNetworkPath = serverNetworkPath;
    }

    public boolean isNetworkDriveMounted() throws Exception
    {
        long counter = 0;
        File mountedDrive = Paths.get(getLocalVolumePath()).toFile();

        while (counter < 20 && !mountedDrive.exists())
        {
            TimeUnit.MILLISECONDS.sleep(200);
            counter++;
        }

        LOG.info("Verify network mounted drive : {}, Mounted: {} ", getLocalVolumePath(), mountedDrive.exists());
        return mountedDrive.exists();
    }

    public void assertNetworkDriveIsMounted() throws Exception
    {
        Assert.assertTrue(isNetworkDriveMounted(), String.format("Network Drive [{}] was mounted successfully", getLocalVolumePath()));
    }

    public void assertNetworkDriveIsUmounted() throws Exception
    {
        Assert.assertFalse(isNetworkDriveMounted(), String.format("Network Drive [{}] was umounted successfully", getLocalVolumePath()));
    }

    public File createFile(String relativePathToLocalVolumePath) throws IOException
    {
        File newFile = Paths.get(getLocalVolumePath().toString(), relativePathToLocalVolumePath).toFile();
        LOG.info("Create a new file {}, in mapped network drive", newFile.getPath());
        newFile.createNewFile();
        return newFile;
    }

    public File createFolder(String relativePathToLocalVolumePath) throws IOException
    {
        File newFolder = Paths.get(getLocalVolumePath().toString(), relativePathToLocalVolumePath).toFile();
        LOG.info("Create a new folder {}, in mapped network drive", newFolder.getPath());
        newFolder.mkdir();
        return newFolder;
    }

    public void deleteContent(String relativePathToLocalVolumePath) throws IOException
    {
        File newFile = Paths.get(getLocalVolumePath().toString(), relativePathToLocalVolumePath).toFile();
        LOG.info("Delete content {} from mapped network drive", newFile.getPath());
        newFile.delete();
    }

    public File renameContent(String originalRelativePath, String renamedRelativePath) throws IOException
    {
        File originalFile = Paths.get(originalRelativePath).toFile();
        File renamedFile = Paths.get(getLocalVolumePath().toString(), renamedRelativePath).toFile();
        LOG.info("Rename content {} as {}, in mapped network drive", originalFile.getPath(), renamedFile.getPath());
        originalFile.renameTo(renamedFile);
        return renamedFile;
    }

    public File copyFile(String sourceRelativePath, String destinationRelativePath) throws IOException
    {
        File sourceContent = Paths.get(getLocalVolumePath().toString(), sourceRelativePath).toFile();
        File destinationContent = Paths.get(getLocalVolumePath().toString(), destinationRelativePath).toFile();

        LOG.info("Copy file {} to {}, in mapped network drive", sourceContent.getPath(), destinationContent.getPath());
        FileUtils.copyFile(sourceContent, destinationContent);
        return destinationContent;
    }

    public File copyFolder(String sourceRelativePath, String destinationRelativePath) throws IOException
    {
        File sourceContent = Paths.get(getLocalVolumePath().toString(), sourceRelativePath).toFile();
        File destinationContent = Paths.get(getLocalVolumePath().toString(), destinationRelativePath).toFile();

        LOG.info("Copy folder {} to {}, in mapped network drive", sourceContent.getPath(), destinationContent.getPath());
        FileUtils.copyDirectory(sourceContent, destinationContent);
        return destinationContent;
    }

    public File moveFile(String sourceRelativePath, String destinationRelativePath) throws IOException
    {
        File sourceContent = Paths.get(getLocalVolumePath().toString(), sourceRelativePath).toFile();
        File destinationContent = Paths.get(getLocalVolumePath().toString(), destinationRelativePath).toFile();

        LOG.info("Move file from {} to {}, in mapped network drive", sourceContent.getPath(), destinationContent.getPath());
        FileUtils.moveFile(sourceContent, destinationContent);
        return destinationContent;
    }

    public File moveFolder(String sourceRelativePath, String destinationRelativePath) throws IOException
    {
        File sourceContent = Paths.get(getLocalVolumePath().toString(), sourceRelativePath).toFile();
        File destinationContent = Paths.get(getLocalVolumePath().toString(), destinationRelativePath).toFile();

        LOG.info("Move folder from {} to {}, in mapped network drive", sourceContent.getPath(), destinationContent.getPath());
        FileUtils.moveDirectory(sourceContent, destinationContent);
        return destinationContent;
    }
}
