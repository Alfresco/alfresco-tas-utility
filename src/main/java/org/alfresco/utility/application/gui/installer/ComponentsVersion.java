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

@Component
public class ComponentsVersion extends ACSWizard{
    @Autowired
    ACSInstallerProperties installerProperties;

    private String getProcessOutputForWin(String command) throws IOException, InterruptedException {
        StringBuilder sb = new StringBuilder();
        try {
            Process executionProcess = Runtime.getRuntime().exec("cmd /c " + command);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(executionProcess.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(executionProcess.getErrorStream()));
            String s = null;
            while ((s = stdInput.readLine()) != null) {
                sb.append(s);
                sb.append("\n");
            }
            while ((s = stdError.readLine()) != null) {
                sb.append(s);
                sb.append("\n");
            }
            stdInput.close();
            stdError.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }

    public void assertJREVersionIs(String expectedVersion) throws IOException, InterruptedException {
        if (SystemUtils.IS_OS_WINDOWS) {
            String command = String.format("%s\\%s", installerProperties.getInstallerDestinationPath().getPath(), "java\\bin\\java.exe -version");
            LOG.info("Java actual version: " + getProcessOutputForWin(command));
            Assert.assertTrue(getProcessOutputForWin(command).contains(expectedVersion), "Java version is not correct, expected: " + expectedVersion);
        } else {
            if (SystemUtils.IS_OS_LINUX) {
                String command = String.format("./%s, %s", installerProperties.getInstallerDestinationPath().getPath(), "java/bin/java -version");
                Assert.assertTrue((Utility.executeOnUnix(command)).contains(expectedVersion), "Java version is not correct, expected: " + expectedVersion);
            }
            if (SystemUtils.IS_OS_MAC) {
                //TODO write code for IS_OS_MAC case
            }
        }
    }


    public void assertTomcatVersionIs(String expectedVersion) throws Exception{
        if(SystemUtils.IS_OS_WINDOWS){
            String command = String.format("%s\\%s", installerProperties.getInstallerDestinationPath(), "tomcat\\bin\\version.sh -version");
            LOG.info("Tomcat actual version: "+ getProcessOutputForWin(command));
            Assert.assertTrue((getProcessOutputForWin(command)).contains(expectedVersion));
        }else{
            if(SystemUtils.IS_OS_LINUX){
                String command = String.format("./%s, %s", installerProperties.getInstallerDestinationPath().getPath(), "tomcat/bin/version.sh -version");
                Assert.assertTrue((Utility.executeOnUnix(command)).contains(expectedVersion));
            }
            if(SystemUtils.IS_OS_MAC) {
                //TODO write code for IS_OS_MAC case
            }
        }
    }

    public void assertJDBCIs(String expectedJDBC) throws IOException {
        if(SystemUtils.IS_OS_WINDOWS){
            Utility.assertFileExists(String.format("%s\\%s\\%s", installerProperties.getInstallerDestinationPath().getPath(),"tomcat\\lib",expectedJDBC));
        }else{
            if(SystemUtils.IS_OS_LINUX){
                Utility.assertFileExists(String.format("%s\\%s\\%s", installerProperties.getInstallerDestinationPath().getPath(), "tomcat/lib", expectedJDBC));
            }
            if(SystemUtils.IS_OS_MAC) {
                //TODO write code for IS_OS_MAC case
            }
        }
    }

    public void assertPsqlVersionIs(String expectedPsqlVersion) throws IOException, InterruptedException {
        if(SystemUtils.IS_OS_WINDOWS) {
            String command = String.format("%s\\%s", installerProperties.getInstallerDestinationPath().getPath(), "postgresql\\bin\\psql --version");
            LOG.info("psql version is: " + getProcessOutputForWin(command));
            Assert.assertTrue((getProcessOutputForWin(command)).contains(expectedPsqlVersion));
        }else{
                if(SystemUtils.IS_OS_LINUX)
                {
                    String command =String.format("./%s, %s", installerProperties.getInstallerDestinationPath().getPath(),"postgresql\\bin\\psql --version");
                    Utility.executeOnUnix(command).contains(expectedPsqlVersion);
                }
                if(SystemUtils.IS_OS_MAC)
                {
                    //TODO write code for IS_OS_MAC case
                }
            }
        }

        public void assertImageMagickVersionIs(String expectedImageMagickVersion) throws IOException, InterruptedException {
        if(SystemUtils.IS_OS_WINDOWS){
            String command = String.format("%s\\%s", installerProperties.getInstallerDestinationPath().getPath(), "imagemagick\\convert --version");
            LOG.info("ImageMagick version is: "+ getProcessOutputForWin(command));
            Assert.assertTrue((getProcessOutputForWin(command)).contains(expectedImageMagickVersion));
        }else
        {if(SystemUtils.IS_OS_LINUX){
            LOG.info("ImageMagick version is: "+ Utility.executeOnUnix(String.format("./%s\\%s", installerProperties.getInstallerDestinationPath().getPath(), "\\common\\convert --version")).contains(expectedImageMagickVersion));
        }
            Assert.assertTrue(Utility.executeOnUnix(String.format("./%s\\%s", installerProperties.getInstallerDestinationPath().getPath(), "\\common\\convert --version")).contains(expectedImageMagickVersion));
        }
            if(SystemUtils.IS_OS_MAC) {
                //TODO write code for IS_OS_MAC case
            }
        }

    @Override
    public GuiScreen focus() throws Exception {
        return null;
    }
}
