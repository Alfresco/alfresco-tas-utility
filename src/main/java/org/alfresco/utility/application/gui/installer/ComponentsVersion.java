package org.alfresco.utility.application.gui.installer;

import org.alfresco.utility.Utility;
import org.alfresco.utility.application.gui.GuiScreen;
import org.apache.commons.lang.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.testng.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Mirela Tifui on 7/12/2017.
 */

@Component public class ComponentsVersion extends ACSWizard
{
    @Autowired ACSInstallerProperties installerProperties;

    public void assertJREVersionIs(String expectedVersion) throws IOException, InterruptedException
    {
        if (SystemUtils.IS_OS_WINDOWS)
        {
            String commandToExecute = String.format("%s\\%s", installerProperties.getInstallerDestinationPath().getPath(), "java\\bin\\java.exe -version");
            LOG.info("Java actual version: " + Utility.getProcessOutputForWin(commandToExecute));
            Assert.assertTrue(Utility.getProcessOutputForWin(commandToExecute).contains(expectedVersion), "Java version is not correct, expected: " + expectedVersion);
        }
        else
        {
            if (SystemUtils.IS_OS_LINUX)
            {
                String commandToExecute = String.format("./%s, %s", installerProperties.getInstallerDestinationPath().getPath(), "java/bin/java -version");
                Assert.assertTrue((Utility.executeOnUnix(commandToExecute)).contains(expectedVersion), "Java version is not correct, expected: " + expectedVersion);
            }
            if (SystemUtils.IS_OS_MAC)
            {
                //TODO write code for IS_OS_MAC case
            }
        }
    }

    public void assertTomcatVersionIs(String expectedVersion) throws Exception
    {
        if (SystemUtils.IS_OS_WINDOWS)
        {
            String commandToExecute = String.format("%s\\%s", installerProperties.getInstallerDestinationPath(), "tomcat\\bin\\version.sh -version");
            LOG.info("Tomcat actual version: " + Utility.getProcessOutputForWin(commandToExecute));
            Assert.assertTrue((Utility.getProcessOutputForWin(commandToExecute)).contains(expectedVersion));
        }
        else
        {
            if (SystemUtils.IS_OS_LINUX)
            {
                String commandToExecute = String.format("./%s, %s", installerProperties.getInstallerDestinationPath().getPath(), "tomcat/bin/version.sh -version");
                Assert.assertTrue((Utility.executeOnUnix(commandToExecute)).contains(expectedVersion));
            }
            if (SystemUtils.IS_OS_MAC)
            {
                //TODO write code for IS_OS_MAC case
            }
        }
    }

    public void assertJDBCIs(String expectedJDBC) throws IOException
    {
        if (SystemUtils.IS_OS_WINDOWS)
        {
            Utility.assertFileExists(String.format("%s\\%s\\%s", installerProperties.getInstallerDestinationPath().getPath(), "tomcat\\lib", expectedJDBC));
        }
        else
        {
            if (SystemUtils.IS_OS_LINUX)
            {
                Utility.assertFileExists(String.format("%s\\%s\\%s", installerProperties.getInstallerDestinationPath().getPath(), "tomcat/lib", expectedJDBC));
            }
            if (SystemUtils.IS_OS_MAC)
            {
                //TODO write code for IS_OS_MAC case
            }
        }
    }

    public void assertPsqlVersionIs(String expectedPsqlVersion) throws IOException, InterruptedException
    {
        if (SystemUtils.IS_OS_WINDOWS)
        {
            String commandToExecute = String.format("%s\\%s", installerProperties.getInstallerDestinationPath().getPath(), "postgresql\\bin\\psql --version");
            LOG.info("psql version is: " + Utility.getProcessOutputForWin(commandToExecute));
            Assert.assertTrue((Utility.getProcessOutputForWin(commandToExecute)).contains(expectedPsqlVersion));
        }
        else
        {
            if (SystemUtils.IS_OS_LINUX)
            {
                String commandToExecute = String.format("./%s, %s", installerProperties.getInstallerDestinationPath().getPath(), "postgresql\\bin\\psql --version");
                Utility.executeOnUnix(commandToExecute).contains(expectedPsqlVersion);
            }
            if (SystemUtils.IS_OS_MAC)
            {
                //TODO write code for IS_OS_MAC case
            }
        }
    }

    public void assertImageMagickVersionIs(String expectedImageMagickVersion) throws IOException, InterruptedException
    {
        if (SystemUtils.IS_OS_WINDOWS)
        {
            String commandToExecute = String.format("%s\\%s", installerProperties.getInstallerDestinationPath().getPath(), "imagemagick\\convert --version");
            LOG.info("ImageMagick version is: " + Utility.getProcessOutputForWin(commandToExecute));
            Assert.assertTrue((Utility.getProcessOutputForWin(commandToExecute)).contains(expectedImageMagickVersion));
        }
        else
        {
            if (SystemUtils.IS_OS_LINUX)
            {
                LOG.info("ImageMagick version is: " + Utility
                        .executeOnUnix(String.format("./%s\\%s", installerProperties.getInstallerDestinationPath().getPath(), "\\common\\convert --version"))
                        .contains(expectedImageMagickVersion));
            }
            Assert.assertTrue(
                    Utility.executeOnUnix(String.format("./%s\\%s", installerProperties.getInstallerDestinationPath().getPath(), "\\common\\convert --version"))
                            .contains(expectedImageMagickVersion));
        }
        if (SystemUtils.IS_OS_MAC)
        {
            //TODO write code for IS_OS_MAC case
        }
    }

    @Override public GuiScreen focus() throws Exception
    {
        return null;
    }
}
