package org.alfresco.utility.application.gui.installer;

public interface Installable 
{
    <T> T waitForInstallerToOpen() throws Exception;
}
