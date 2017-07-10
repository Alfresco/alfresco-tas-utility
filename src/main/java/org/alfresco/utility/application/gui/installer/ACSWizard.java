package org.alfresco.utility.application.gui.installer;

import java.io.File;
import java.nio.file.Paths;

import org.alfresco.utility.Utility;
import org.alfresco.utility.application.gui.GuiScreen;
import org.apache.commons.lang.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ACSWizard extends GuiScreen
{
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
        else
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
}
