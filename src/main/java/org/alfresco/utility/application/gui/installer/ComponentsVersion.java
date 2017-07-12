package org.alfresco.utility.application.gui.installer;

import org.alfresco.utility.Utility;
import org.alfresco.utility.application.gui.GuiScreen;
import org.apache.commons.lang.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;


/**
 * Created by Mirela Tifui on 7/12/2017.
 */

@Component
public class ComponentsVersion extends ACSWizard{
    @Autowired
    ACSInstallerProperties installerProperties;

    public void assertJREVersionIs(String expectedVersion) throws IOException {
        if (SystemUtils.IS_OS_WINDOWS) {
            LOG.info("Java actual version: "+ Utility.executeOnWin(String.format("%s\\%s", installerProperties.getInstallerDestinationPath().getPath(), "java\\bin\\java.exe -version")));
            Assert.assertTrue(Utility.executeOnWin(String.format("%s\\%s", installerProperties.getInstallerDestinationPath().getPath(), "java\\bin\\java.exe -version")).contains(expectedVersion), "Java version is not correct, expected: "+ expectedVersion);
        } else {
            if (SystemUtils.IS_OS_LINUX) {
                Assert.assertTrue(Utility.executeOnUnix(String.format("./%s, %s", installerProperties.getInstallerDestinationPath().getPath(), "java/bin/java -version")).contains(expectedVersion), "Java version is not correct, expected: "+ expectedVersion);
            }
            //TODO write method for IS_OS_MAC
        }
    }

    public void assertTomcatVersionIs(String expectedVersion) throws Exception{
        if(SystemUtils.IS_OS_WINDOWS){
            LOG.info("Tomcat actual version: "+ Utility.executeOnWin(String.format("%s\\%s", installerProperties.getInstallerDestinationPath(), "tomcat\\bin\\version.sh -version")));
            Assert.assertTrue(Utility.executeOnWin(String.format("%s\\%s", installerProperties.getInstallerDestinationPath(), "tomcat\\bin>version.sh -version")).contains(expectedVersion));
        }else{
            if(SystemUtils.IS_OS_LINUX){
                Assert.assertTrue(Utility.executeOnUnix(String.format("./%s, %s", installerProperties.getInstallerDestinationPath().getPath(), "tomcat/bin/version.sh -version")).contains(expectedVersion));
            }
        }
    }

    public void assertJDBCIs(String expectedJDBC) throws IOException {
        if(SystemUtils.IS_OS_WINDOWS){
            Utility.assertFileExists(String.format("%s\\%s\\%s", installerProperties.getInstallerDestinationPath().getPath(),"tomcat\\lib",expectedJDBC));
        }
    }
    @Override
    public GuiScreen focus() throws Exception {
        return null;
    }
}
