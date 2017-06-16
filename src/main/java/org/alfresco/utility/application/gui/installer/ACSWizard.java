package org.alfresco.utility.application.gui.installer;

import java.io.File;
import java.nio.file.Paths;

import org.alfresco.utility.Utility;
import org.alfresco.utility.application.Applicationable;
import org.alfresco.utility.application.gui.GuiScreen;

public abstract class ACSWizard extends GuiScreen implements Applicationable
{

    @Override
    public ACSWizard open() throws Exception
    {
        File mountDefaultLocation = Paths.get("/Volumes/Alfresco Content Services/".replaceAll(" ", "\\\\ ")).toFile();

        Utility.executeOnUnixNoWait(
                mountDefaultLocation.getPath() + "/alfresco-content-services-installer-5.2.1-SNAPSHOT-osx-x64.app/Contents/MacOS/installbuilder.sh");

        Thread.sleep(5000);
        return this;
    }

    @Override
    public ACSWizard close() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getProcessName()
    {
        return "Alfresco Content Service";
    }

}
