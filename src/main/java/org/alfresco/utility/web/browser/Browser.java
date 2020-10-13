package org.alfresco.utility.web.browser;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.alfresco.utility.TasProperties;
import org.alfresco.utility.Utility;
import org.alfresco.utility.exception.UnrecognizedBrowser;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;

/**
 * Return differed DesiredCapabilities for {@link WebDriver}
 * 
 * @author Paul.Brodner
 */
public enum Browser
{
    FIREFOX(DesiredCapabilities.firefox()),
    CHROME(DesiredCapabilities.chrome()),
    //HTMLUNIT(DesiredCapabilities.htmlUnit()),
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
    public static FirefoxOptions setFirefoxOptions(TasProperties properties)
    {
        FirefoxOptions options = new FirefoxOptions();
        options.addPreference("browser.download.dir", getDownloadLocation());
        options.addPreference("browser.download.folderList", 2);
        options.addPreference("browser.download.manager.alertOnEXEOpen", false);
        options.addPreference("browser.helperApps.neverAsk.saveToDisk",
                    "application/msword, application/csv, application/ris, text/csv, image/png, application/pdf, text/html, text/plain, "
                    + "application/zip, application/x-zip, application/x-zip-compressed, application/download, application/octet-stream, "
                    + "application/vnd.openxmlformats-officedocument.wordprocessingml.document,"
                    + "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                    + "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        options.addPreference("browser.download.manager.showWhenStarting", false);
        options.addPreference("browser.download.manager.focusWhenStarting", false);
        options.addPreference("browser.download.useDownloadDir", true);
        options.addPreference("browser.helperApps.alwaysAsk.force", false);
        options.addPreference("browser.download.manager.alertOnEXEOpen", false);
        options.addPreference("browser.download.manager.closeWhenDone", true);
        options.addPreference("browser.download.manager.showAlertOnComplete", false);
        options.addPreference("intl.accept_languages", getBrowserLanguage(properties));
        options.setAcceptInsecureCerts(true);
        return options;
    }

    public static WebDriver fromProperties(TasProperties properties)
    {
        switch (properties.getBrowserName().toLowerCase())
        {
            case "firefox":
                setFirefoxDriver();
                if (SystemUtils.IS_OS_LINUX)
                {
                    FirefoxBinary firefoxBinary = new FirefoxBinary();
                    firefoxBinary.addCommandLineOptions("--headless");
                    Map<String, String> env = new HashMap<String, String>();
                    env.put("DISPLAY", ":" + properties.getDisplayXport());
                    FirefoxOptions options1 = setFirefoxOptions(properties);
                    options1.setBinary(firefoxBinary);
                    return new FirefoxDriver(new GeckoDriverService.Builder().withEnvironment(env).build(), options1);
                }
                else
                {
                    return new FirefoxDriver(setFirefoxOptions(properties));
                }
            case "chrome":
                setChromeDriver();
                HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
                chromePrefs.put("profile.default_content_settings.popups", 0);
                chromePrefs.put("download.default_directory", getDownloadLocation());

                ChromeOptions options = new ChromeOptions();
                options.addArguments("--start-maximized");
                options.addArguments(String.format("--lang=%s", getBrowserLanguage(properties)));
                options.setExperimentalOption("prefs", chromePrefs);
                ChromeDriver chromeDriver = new ChromeDriver(options);
                return chromeDriver;
            case "ie":
                return new InternetExplorerDriver();
            //case "htmlunit":
                //HtmlUnitDriver driver = new HtmlUnitDriver(BrowserVersion.BEST_SUPPORTED);
                //driver.setJavascriptEnabled(true);
                //return driver;
            case "safari":
                return new SafariDriver();
            default:
                throw new UnrecognizedBrowser(properties.getBrowserName());
        }
    }
    
    private static void setChromeDriver()
    {
        String chromedriver = "not-defined";
        if (SystemUtils.IS_OS_WINDOWS)
            chromedriver = "shared-resources/chromedriver/chromedriver.exe";
        else if (SystemUtils.IS_OS_MAC)
        {
            chromedriver = "shared-resources/chromedriver/chromedriver_mac";
            Utility.getTestResourceFile(chromedriver).setExecutable(true);
        }
        else
        {
            chromedriver = "shared-resources/chromedriver/chromedriver_linux";
            Utility.getTestResourceFile(chromedriver).setExecutable(true);
        }
        System.setProperty("webdriver.chrome.driver", Utility.getTestResourceFile(chromedriver).toString());
    }
    
    private static void setFirefoxDriver()
    {
        String geckodriver = "Not-Defined";
        if (SystemUtils.IS_OS_WINDOWS)
            geckodriver = "shared-resources/geckodriver/geckodriver.exe";
        else if (SystemUtils.IS_OS_MAC)
        {
            geckodriver = "shared-resources/geckodriver/geckodriver_mac";
            Utility.getTestResourceFile(geckodriver).setExecutable(true);
        }
        else
        {
            geckodriver = "shared-resources/geckodriver/geckodriver_linux";
            Utility.getTestResourceFile(geckodriver).setExecutable(true);
        }
        System.setProperty("webdriver.gecko.driver", Utility.getTestResourceFile(geckodriver).toString());
    }

    private static String getDownloadLocation()
    {
        String srcRoot = System.getProperty("user.dir") + File.separator;
        String testDataFolder = srcRoot + "testdata" + File.separator;
        return testDataFolder;
    }
    
    private static String getBrowserLanguage(TasProperties properties)
    {
        if(!StringUtils.isEmpty(properties.getBrowserLanguageCountry()))
        {
            return properties.getBrowserLanguage() + "-" +  properties.getBrowserLanguageCountry();
        }
        return properties.getBrowserLanguage();
    }
    
   
}
