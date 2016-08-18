package org.alfresco.utility.network;

import java.io.File;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import org.alfresco.utility.LogFactory;
import org.alfresco.utility.data.DataValue;
import org.alfresco.utility.model.UserModel;
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
        String cmdWithArgs = String.format(command, arguments);
        LOG.info("Running command {}", cmdWithArgs);
        Process process = Runtime.getRuntime().exec(cmdWithArgs);
        process.waitFor();
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
        LOG.info("Verify network mounted drive : {}", getLocalVolumePath());
        long counter = 0;
        File mountedDrive = Paths.get(getLocalVolumePath()).toFile();
        while (counter < 20 && !mountedDrive.exists())
        {
            TimeUnit.MILLISECONDS.sleep(200);
            counter++;
        }
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
}
