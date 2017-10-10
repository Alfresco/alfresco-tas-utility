package org.alfresco.utility.web.browser;

import java.io.File;
import java.util.HashMap;

import org.alfresco.utility.TasProperties;
import org.alfresco.utility.Utility;
import org.alfresco.utility.exception.UnrecognizedBrowser;
import org.apache.commons.lang.SystemUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;

import com.gargoylesoftware.htmlunit.BrowserVersion;

/**
 * Return differed DesiredCapabilities for {@link WebDriver}
 * 
 * @author Paul.Brodner
 */
public enum Browser
{
    FIREFOX(DesiredCapabilities.firefox()),
    CHROME(DesiredCapabilities.chrome()),
    HTMLUNIT(DesiredCapabilities.htmlUnit()),
    INTERNETEXPLORER(DesiredCapabilities.internetExplorer()),
    OPERA(DesiredCapabilities.operaBlink()),
    HTMLINITDRIVER(DesiredCapabilities.htmlUnit()),

    /*
     * Add Safari-Driver extension prior to tests
     * http://selenium-release.storage.googleapis.com/2.48/SafariDriver.safariextz
     */
    SAFARI(DesiredCapabilities.safari());

    public static Browser getBrowserFromProperties(TasProperties properties)
    {
        return valueOf(properties.getBrowserName().toUpperCase());
    }

    private final DesiredCapabilities capabilities;

    Browser(DesiredCapabilities caps)
    {
        this.capabilities = caps;
    }

    public DesiredCapabilities getCapabilities()
    {
        return capabilities;
    }

    /*
     * Change Firefox browser's default download location to testdata folder
     * return type : FirefoxProfile
     */
    public static FirefoxProfile changeFirefoxDownloadLocationToTestDataFolder()
    {
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("browser.download.dir", getDownloadLocation());
        profile.setPreference("browser.download.folderList", 2);
        profile.setPreference("browser.download.manager.alertOnEXEOpen", false);
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
                "application/msword, application/csv, application/ris, text/csv, image/png, application/pdf, text/html, text/plain, application/zip, application/x-zip, application/x-zip-compressed, application/download, application/octet-stream");
        profile.setPreference("browser.download.manager.showWhenStarting", false);
        profile.setPreference("browser.download.manager.focusWhenStarting", false);
        profile.setPreference("browser.download.useDownloadDir", true);
        profile.setPreference("browser.helperApps.alwaysAsk.force", false);
        profile.setPreference("browser.download.manager.alertOnEXEOpen", false);
        profile.setPreference("browser.download.manager.closeWhenDone", true);
        profile.setPreference("browser.download.manager.showAlertOnComplete", false);
        return profile;
    }


    public static WebDriver fromProperties(TasProperties properties)
    {
        switch (properties.getBrowserName().toLowerCase())
        {                
            case "firefox":
                String geckodriver = "Not-Defined";
                if(SystemUtils.IS_OS_WINDOWS)
                    geckodriver = "shared-resources/geckodriver/geckodriver.exe";
                else if(SystemUtils.IS_OS_MAC)
                {
                    geckodriver = "shared-resources/geckodriver/geckodriver_mac";
                    Utility.getTestResourceFile(geckodriver).setExecutable(true);
                }        
                else
                {
                    geckodriver = "shared-resources/geckodriver/geckodriver_linux";
                    Utility.getTestResourceFile(geckodriver).setExecutable(true);
                }
                System.setProperty("webdriver.gecko.driver",Utility.getTestResourceFile(geckodriver).toString());
                //return new FirefoxDriver(changeFirefoxDownloadLocationToTestDataFolder());
                return new FirefoxDriver();
            case "chrome":
                System.setProperty("webdriver.chrome.driver", properties.getEnv().getProperty("browser.chrome.driver"));
                HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
                chromePrefs.put("profile.default_content_settings.popups", 0);
                chromePrefs.put("download.default_directory", getDownloadLocation());
                
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--start-maximized");
                options.setExperimentalOption("prefs", chromePrefs);
                ChromeDriver chromeDriver = new ChromeDriver(options);
                return chromeDriver;
            case "ie":
                return new InternetExplorerDriver();
            case "htmlunit":
                HtmlUnitDriver driver = new HtmlUnitDriver(BrowserVersion.BEST_SUPPORTED);
                driver.setJavascriptEnabled(true);
                
                return driver;
            case "safari":
                return new SafariDriver();
            default:
                throw new UnrecognizedBrowser(properties.getBrowserName());
        }
    }
    
    private static String getDownloadLocation()
    {
        String srcRoot = System.getProperty("user.dir") + File.separator;
        String testDataFolder = srcRoot + "testdata" + File.separator;
        return testDataFolder;
    }
}
