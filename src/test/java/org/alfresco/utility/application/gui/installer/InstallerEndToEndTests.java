package org.alfresco.utility.application.gui.installer;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * Created by Claudia Agache on 7/10/2017.
 */
public class InstallerEndToEndTests extends InstallerTest
{
    @Autowired
    ACSUninstaller uninstaller;
    public void installationWithDefaultParameters()
    {
        //TODO
    }

    public void installationInAdvancedMode()
    {
        //TODO
    }

    public void verifyingComponentsVersion()
    {
        //TODO
    }

    @Test
    public void uninstall() throws Exception {
       uninstaller.open();
       System.out.println("test");
    }
}
