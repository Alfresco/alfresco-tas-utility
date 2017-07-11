package org.alfresco.utility.application.gui.installer;

import org.alfresco.utility.Utility;
import org.alfresco.utility.application.gui.GuiScreen;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.SystemUtils;
import org.springframework.stereotype.Component;

@Component
public class ACSUninstaller extends ACSWizard
{

    @Override
    public GuiScreen focus() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public ACSWizard open() throws Exception {
        if (SystemUtils.IS_OS_WINDOWS)
        {
            Utility.executeOnWin(String.format("%s\\%s", installerProperties.getInstallerDestinationPath().getPath(), installerProperties.getOSProperty("uninstaller.name")));
            Thread.sleep(8000);
        }
        else
        {
            if(SystemUtils.IS_OS_LINUX)
                Utility.executeOnUnix(String.format("./%s", installerProperties.getInstallerDestinationPath().getPath(), installerProperties.getOSProperty("uninstaller.name")));
            throw new Exception("add supported code for this method");
        }

        return this;
    }
    

}
