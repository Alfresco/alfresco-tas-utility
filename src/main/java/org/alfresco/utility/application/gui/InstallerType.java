package org.alfresco.utility.application.gui;

import org.alfresco.utility.report.log.Step;

public class InstallerType
{ 
    private static String installerType;

    public static String getInstallerType()
    {
        return installerType;
    }

    public static void setInstallerType(String name)
    {
        Step.STEP("Setting installer type based on installer source");
        if(name.contains("share"))
            installerType = "share";
        else if (name.contains("platform"))
            installerType = "platform";
        else
            installerType = "distribution";
    }

}
