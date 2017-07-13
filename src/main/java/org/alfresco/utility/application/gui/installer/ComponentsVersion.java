package org.alfresco.utility.application.gui.installer;

import org.alfresco.utility.Utility;
import org.alfresco.utility.application.gui.GuiScreen;
import org.apache.commons.lang.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.testng.Assert;

import java.io.IOException;


/**
 * Created by Mirela Tifui on 7/12/2017.
 */

@Component
public class ComponentsVersion extends ACSWizard{
    @Autowired
    ACSInstallerProperties installerProperties;

    public void assertJREVersionIs(String expectedVersion) throws IOException {
//        if (SystemUtils.IS_OS_WINDOWS) {
//            LOG.info("Java actual version: "+ Utility.executeOnWin(String.format("%s\\%s", installerProperties.getInstallerDestinationPath().getPath(), "java\\bin\\java.exe -version")));
//            Assert.assertTrue(Utility.executeOnWin(String.format("%s\\%s", installerProperties.getInstallerDestinationPath().getPath(), "java\\bin\\java.exe -version")).contains(expectedVersion), "Java version is not correct, expected: "+ expectedVersion);
//        } else {
//            if (SystemUtils.IS_OS_LINUX) {
//                Assert.assertTrue(Utility.executeOnUnix(String.format("./%s, %s", installerProperties.getInstallerDestinationPath().getPath(), "java/bin/java -version")).contains(expectedVersion), "Java version is not correct, expected: "+ expectedVersion);
//            }
//            if(SystemUtils.IS_OS_MAC) {
//                //TODO write code for IS_OS_MAC case
//            }
//        }
    }

    public void assertTomcatVersionIs(String expectedVersion) throws Exception{
//        if(SystemUtils.IS_OS_WINDOWS){
//            LOG.info("Tomcat actual version: "+ Utility.executeOnWin(String.format("%s\\%s", installerProperties.getInstallerDestinationPath(), "tomcat\\bin\\version.sh -version")));
//            Assert.assertTrue(Utility.executeOnWin(String.format("%s\\%s", installerProperties.getInstallerDestinationPath(), "tomcat\\bin>version.sh -version")).contains(expectedVersion));
//        }else{
//            if(SystemUtils.IS_OS_LINUX){
//                Assert.assertTrue(Utility.executeOnUnix(String.format("./%s, %s", installerProperties.getInstallerDestinationPath().getPath(), "tomcat/bin/version.sh -version")).contains(expectedVersion));
//            }
//            if(SystemUtils.IS_OS_MAC) {
//                //TODO write code for IS_OS_MAC case
//            }
//        }
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

    public void assertPsqlVersionIs(String expectedPsqlVersion) throws IOException {
//        if(SystemUtils.IS_OS_WINDOWS) {
//            LOG.info("psql version is: " + Utility.executeOnWin(String.format("%s\\%s", installerProperties.getInstallerDestinationPath().getPath(), "postgresql\\bin\\psql --version")));
//            Assert.assertTrue(Utility.executeOnWin(String.format("%s\\%s", installerProperties.getInstallerDestinationPath().getPath(), "postgresql\\bin\\psql --version")).contains(expectedPsqlVersion));
//        }else{
//                if(SystemUtils.IS_OS_LINUX){
//                    Utility.executeOnUnix(String.format("./%s, %s", installerProperties.getInstallerDestinationPath().getPath(),"postgresql\\bin\\psql --version" )).contains(expectedPsqlVersion);
//                }
//                if(SystemUtils.IS_OS_MAC) {
//                    //TODO write code for IS_OS_MAC case
//                }
//            }
        }

        public void assertImageMagickVersionIs(String expectedImageMagickVersion) throws IOException {
        if(SystemUtils.IS_OS_WINDOWS){
//            LOG.info("ImageMagick version is: "+ Utility.executeOnWin(String.format("%s\\%s", installerProperties.getInstallerDestinationPath().getPath(), "imagemagick\\convert --version")));
//            Assert.assertTrue(Utility.executeOnWin(String.format("%s\\%s", installerProperties.getInstallerDestinationPath().getPath(), "imagemagick\\convert --version")).contains(expectedImageMagickVersion));
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
