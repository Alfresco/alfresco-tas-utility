package org.alfresco.utility.web.browser;

import java.net.MalformedURLException;

import org.alfresco.utility.TasProperties;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Factory of {@link WebBrowser} object
 * This will initialize automatically local/remote WebDriver based on settings defined *.properties/spring XML
 * file/system environment
 * Take a look on {@link ContextAwareParallelSampleTest} for a simple example on how to use it
 * 
 * @author Paul.Brodner
 * @updated By Swarnajit Adhikary
 */
@Component
public class WebBrowserFactory implements FactoryBean<WebBrowser>
{
    private static final Logger LOG = LoggerFactory.getLogger(WebBrowserFactory.class);

    @Autowired
    TasProperties properties;

    public WebBrowser getWebBrowser() throws Exception {
        WebBrowser webBrowser;

        if (properties.isGridEnabled()) {
            // Remote execution on Selenium Grid
            webBrowser = new WebBrowser(getRemoteWebDriver(properties), properties);
        } else {
            // Local execution
            webBrowser = new WebBrowser(Browser.fromProperties(properties), properties);
        }

        // Maximize window for non-Chrome browsers when running locally
        if (!properties.getBrowserName().equalsIgnoreCase("chrome") && !properties.isGridEnabled()) {
            webBrowser.maximize();
        }

        return webBrowser;
    }

    @Override
    public Class<?> getObjectType()
    {
        return WebBrowser.class;
    }

    @Override
    public boolean isSingleton()
    {
        return false;
    }

    /**
     * Get a new {@link }
     * 
     * @param properties
     * @return
     * @throws MalformedURLException
     */
    private static RemoteWebDriver getRemoteWebDriver(TasProperties properties) throws MalformedURLException {
        LOG.info("Using RemoteWebDriver on Hub URL {}", properties.getGridUrl());

        String browserName = properties.getBrowserName().toLowerCase();
        RemoteWebDriver remoteWebDriver;

        switch (browserName) {
            case "firefox":
                FirefoxOptions firefoxOptions = Browser.setFirefoxOptions(properties);
                firefoxOptions.setCapability("browserVersion", properties.getBrowserVersion());
                firefoxOptions.setCapability("platformName", properties.getEnvPlatformName());
                remoteWebDriver = new RemoteWebDriver(properties.getGridUrl(), firefoxOptions);
                break;

            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setCapability("browserVersion", properties.getBrowserVersion());
                chromeOptions.setCapability("platformName", properties.getEnvPlatformName());
                remoteWebDriver = new RemoteWebDriver(properties.getGridUrl(), chromeOptions);
                break;

            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.setCapability("browserVersion", properties.getBrowserVersion());
                edgeOptions.setCapability("platformName", properties.getEnvPlatformName());
                remoteWebDriver = new RemoteWebDriver(properties.getGridUrl(), edgeOptions);
                break;

            default:
                throw new IllegalArgumentException("Unsupported browser: " + properties.getBrowserName());
        }

        return remoteWebDriver;
    }

    @Override
    public WebBrowser getObject() throws Exception
    {
        return getWebBrowser();
    }
}
