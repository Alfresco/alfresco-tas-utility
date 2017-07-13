package org.alfresco.utility.application.gui.installer;

import org.alfresco.utility.LogFactory;
import org.alfresco.utility.Utility;
import org.alfresco.utility.application.gui.GuiScreen;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;

import java.io.File;
import java.nio.file.Paths;

public abstract class ACSWizard extends GuiScreen
{
    protected Logger LOG = LogFactory.getLogger();

    @Autowired
    ACSInstallerProperties installerProperties;

    public ACSInstallerProperties getFileProperties()
    {
        return installerProperties;
    }

    @Override
    public ACSWizard open() throws Exception
    {
        if (SystemUtils.IS_OS_WINDOWS)
        {
            Utility.executeOnWin(String.format("\"%s\"", installerProperties.getInstallerSourcePath().getPath()));
            Thread.sleep(5000);
        }
        else if (SystemUtils.IS_OS_MAC)
        {
            /* mount dmg file to mount point */
            Utility.executeOnUnixNoWait("open " + installerProperties.getInstallerSourcePath().getPath());
            Thread.sleep(2000);

            /*
             * start the installbuilder.sh from mounted volume
             * "/Volumes/Alfresco Content Service/alfresco-content-services-installer-5.2.1-SNAPSHOT-osx-x64.app/Contents/MacOS/installbuilder.sh"
             **/
            File installBuilderSh = Paths
                    .get(String.format("%s/Contents/MacOS/installbuilder.sh", Utility.getMountedApp(installerProperties.getInstallerMountLocation()))).toFile();

            if (!installBuilderSh.exists())
                throw new Exception("Cannot mount Alfresco Installer to:" + installerProperties.getInstallerMountLocation().getPath());

            Utility.executeOnUnixNoWait("sh " + installBuilderSh.getPath().replaceAll(" ", "\\\\ "));
            Thread.sleep(5000);
        }
        else if (SystemUtils.IS_OS_LINUX)
        {
            Utility.executeOnUnixNoWait("chmod +x " + installerProperties.getInstallerSourcePath().getPath());
            Utility.executeOnUnixNoWait(installerProperties.getInstallerSourcePath().getPath());
            Thread.sleep(5000);
        }

        return this;
    }

    @Override
    public ACSWizard close() throws Exception
    {
        if (SystemUtils.IS_OS_MAC)
        {
            Utility.executeOnUnix(String.format("hdiutil detach %s -force", installerProperties.getInstallerMountLocation().getPath()));
        }
        else
        {
            Utility.executeOnWin(String.format("taskkill /F /IM \"%s\"", installerProperties.getOSProperty("installer.name")));
        }

        return this;
    }

    @Override
    public String getProcessName()
    {
        return "alfresco-content-services";
    }

    public void assertInstallationFolderIsEmpty()
    {
        File installationFolder = installerProperties.getInstallerDestinationPath();
        if(installationFolder.exists())
        {
            File[] list = installationFolder.listFiles();
            for (File item : list)
            {
                LOG.info(String.format("Found file/folder: %s", item.getPath()));
            }
            Assert.assertEquals(FileUtils.sizeOf(installationFolder), 0, "Size of install folder(bytes): ");
        }
    }
}
