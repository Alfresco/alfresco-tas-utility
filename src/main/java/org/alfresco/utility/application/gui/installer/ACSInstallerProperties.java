package org.alfresco.utility.application.gui.installer;

import java.io.File;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.testng.asserts.SoftAssert;

@Configuration
@PropertySource(value = "classpath:installer/installer.properties", ignoreResourceNotFound = true)
public class ACSInstallerProperties
{
    @Autowired
    Environment env;
    
    public enum LANGUAGES {
    	FRENCH { public String toString() { return "languageSelection/languages/french"; } },
    	ENGLISH { public String toString() { return "languageSelection/languages/english"; } },
    	SPANISH { public String toString() { return "languageSelection/languages/spanish"; } },
    	ITALIAN { public String toString() { return "languageSelection/languages/italian"; } },
    	GERMAN { public String toString() { return "languageSelection/languages/german"; } },
    	JAPANESE { public String toString() { return "languageSelection/languages/japanese"; } },
    	DUTCH { public String toString() { return "languageSelection/languages/dutch"; } },
    	RUSSIAN { public String toString() { return "languageSelection/languages/russian"; } },
    	CHINESE { public String toString() { return "languageSelection/languages/chinese"; } },
    	NORWEGIAN { public String toString() { return "languageSelection/languages/norwegian"; } };
    }
    
    public enum DESCRIPTION {
    	FRENCH { public String toString() { return "languageSelection/languages/french/description"; } },
    	ENGLISH { public String toString() { return "languageSelection/languages/english/description"; } };
    }

    public Environment getEnv()
    {
        return env;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer()
    {
        return new PropertySourcesPlaceholderConfigurer();
    }

    /**
     * Return the property based on the key name
     * 
     * @param key
     * @return
     */
    public String getOSProperty(String key)
    {
        String osName = "unix";
        if (SystemUtils.IS_OS_MAC)
            osName = "mac";
        else if (SystemUtils.IS_OS_WINDOWS)
            osName = "win";

        return getEnv().getProperty(String.format("%s.%s", osName, key));
    }

    public String getAdminPassword()
    {
        return getEnv().getProperty("admin.password");
    }

    public String getInstallerDBPort()
    {
        return getEnv().getProperty("installer.db.port");
    }

    public String getInstallerDBUsername()
    {
        return getEnv().getProperty("installer.db.username");
    }

    public String getInstallerDBPassword()
    {
        return getEnv().getProperty("installer.db.password");
    }

    public String getInstallerWebServerDomain()
    {
        return getEnv().getProperty("installer.web.server.domain");
    }

    public String getInstallerTomcatServerPort()
    {
        return getEnv().getProperty("installer.tomcat.serverPort");
    }

    public String getInstallerTomcatShutdownPort()
    {
        return getEnv().getProperty("installer.tomcat.shutdownPort");
    }

    public String getInstallerTomcatSSLPort()
    {
        return getEnv().getProperty("installer.tomcat.sslPort");
    }

    public String getInstallerTomcatAJPPort()
    {
        return getEnv().getProperty("installer.tomcat.ajpPort");
    }

    public String getInstallerFTPPort()
    {
        return getEnv().getProperty("installer.ftpPort");
    }

    public String getInstallerRMIPort()
    {
        return getEnv().getProperty("installer.rmiPort");
    }

    public String getInstallerLibreOfficeServerPort()
    {
        return getEnv().getProperty("installer.win.libre.office.serverPort");
    }

    public String getInstallerRemoteSolrHost()
    {
        return getEnv().getProperty("installer.win.remote.solr.host");
    }

    public String getInstallerRemoteSolrSSLPort()
    {
        return getEnv().getProperty("installer.win.remote.solr.sslPort");
    }

    /**
     * Return the installer.source path as defined in installer.properties file
     * for your OS
     */
    public File getInstallerSourcePath()
    {
        return Paths.get(getOSProperty("installer.source")).toFile();
    }

    /**
     * Return the installer.destination path as defined in installer.properties file
     * for your OS
     */
    public File getInstallerDestinationPath()
    {
        return Paths.get(getOSProperty("installer.destination")).toFile();
    }

    /**
     * Return the installer.mountLocation path as defined in installer.properties file
     * for your MacOS/Unix OS
     */
    public File getInstallerMountLocation()
    {
        return Paths.get(getOSProperty("installer.mountLocation")).toFile();
    }

    /**
     * Return the installer.optionsFile path as defined in installer.properties file
     * for your OS
     */
    public File getInstallerOptionFile()
    {
        return Paths.get(getOSProperty("installer.optionsFile")).toFile();
    }

    /**
     * @return the list of expected files that should be installed on your OS
     */
    public List<String> getExpectedInstalledFiles()
    {
        return getPropertyArrays("expect.file");
    }

    /**
     * @return the list of expected files that should be installed on your OS
     */
    public List<String> getExpectedInstalledRepoAMPs()
    {
        return getPropertyArrays("expect.amps.repo");
    }

    /**
     * @return the list of expected files that should be installed on your OS
     */
    public List<String> getExpectedInstalledShareAMPs()
    {
        return getPropertyArrays("expect.amps.share");
    }

    /**
     * @return the list of expected wars that should be installed on your OS
     */
    public List<String> getExpectedInstalledWARS()
    {
        return getPropertyArrays("expect.war");
    }

    /**
     * <code>
     * mac.expect.file.1=alf_data
     * mac.expect.file.2=alfresco.ico
     * mac.expect.file.3=amps
     * <code>
     * passing "expect.file" will return all data within mac.expect.file.1 and mac.expect.file.3
     * 
     * @param propertyKey
     * @return
     */
    private List<String> getPropertyArrays(String propertyKey)
    {
        List<String> list = new LinkedList<>();

        String file = "";

        for (int i = 1; (file = getOSProperty(String.format("%s.%s", propertyKey, i))) != null; i++)
        {
            list.add(file);
        }
        return list;
    }

    public void assertExpectedFilesAreInstalled()
    {
        SoftAssert softAssert = new SoftAssert();
        for (String filePath : getExpectedInstalledFiles())
        {
            File file = Paths.get(getInstallerDestinationPath().getPath(), filePath).toFile();
            softAssert.assertTrue(file.exists(),
                    String.format(
                            "Missing expected file after installation:  {%s}. [Please check your expected files path defined in \"intaller.properties\" file]",
                            file.getPath()));
        }

        softAssert.assertAll();
    }

    public void assertExpectedAMPSAreInstalled()
    {
        SoftAssert softAssert = new SoftAssert();
        for (String filePath : getExpectedInstalledRepoAMPs())
        {
            File file = Paths.get(getInstallerDestinationPath().getPath(), "amps", filePath).toFile();
            softAssert.assertTrue(file.exists(),
                    String.format(
                            "Missing expected repo amp after installation:  {%s}. [Please check your expected repo amp path defined in \"intaller.properties\" file]",
                            file.getPath()));
        }

        for (String filePath : getExpectedInstalledShareAMPs())
        {
            File file = Paths.get(getInstallerDestinationPath().getPath(), "amps_share", filePath).toFile();
            softAssert.assertTrue(file.exists(),
                    String.format(
                            "Missing expected share amp after installation:  {%s}. [Please check your expected share amp path defined in \"intaller.properties\" file]",
                            file.getPath()));
        }

        softAssert.assertAll();
    }

    public void assertExpectedFilesAreNotInstalled()
    {
        SoftAssert softAssert = new SoftAssert();
        for (String filePath : getExpectedInstalledFiles())
        {
            File file = Paths.get(getInstallerDestinationPath().getPath(), filePath).toFile();
            softAssert.assertFalse(file.exists(),
                    String.format(
                            "Found not expected file after installation:  {%s}. [Please check your expected files path defined in \"intaller.properties\" file]",
                            file.getPath()));
        }

        softAssert.assertAll();
    }

    public void assertExpectedAMPSAreNotInstalled()
    {
        SoftAssert softAssert = new SoftAssert();
        for (String filePath : getExpectedInstalledRepoAMPs())
        {
            File file = Paths.get(getInstallerDestinationPath().getPath(), "amps", filePath).toFile();
            softAssert.assertFalse(file.exists(),
                    String.format(
                            "Found not expected repo amp after installation:  {%s}. [Please check your expected repo amp path defined in \"intaller.properties\" file]",
                            file.getPath()));
        }

        for (String filePath : getExpectedInstalledShareAMPs())
        {
            File file = Paths.get(getInstallerDestinationPath().getPath(), "amps_share", filePath).toFile();
            softAssert.assertFalse(file.exists(),
                    String.format(
                            "Found not expected share amp after installation:  {%s}. [Please check your expected share amp path defined in \"intaller.properties\" file]",
                            file.getPath()));
        }

        softAssert.assertAll();
    }

    public void assertExpectedWARSAreInstalled()
    {
        SoftAssert softAssert = new SoftAssert();
        for (String filePath : getExpectedInstalledWARS())
        {
            File file = Paths.get(getInstallerDestinationPath().getPath(), "tomcat", "webapps", filePath).toFile();
            softAssert.assertTrue(file.exists(),
                    String.format(
                            "Missing expected war after installation:  {%s}. [Please check your expected wars path defined in \"intaller.properties\" file]",
                            file.getPath()));
        }

        softAssert.assertAll();
    }
}
