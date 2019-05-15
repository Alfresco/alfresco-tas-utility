package org.alfresco.utility.data.auth;

import static org.alfresco.utility.report.log.Step.STEP;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.alfresco.utility.TasProperties;
import org.alfresco.utility.Utility;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Created by Claudia Agache on 6/23/2017.
 * https://support.microsoft.com/en-in/help/322684/how-to-use-the-directory-service-command-line-tools-to-manage-active-directory-objects-in-windows-server-2003
 */
@Service
@Scope(value = "prototype")
public class DataNtlmPassthru
{
    @Autowired
    private TasProperties tasProperties;
    private String command, psCommand;
    String userPrefPath = "C:\\Program Files\\Mozilla Firefox\\defaults\\pref";
    String userPrefFile = "userPref.js";

    String firefoxPath = "C:\\Program Files\\Mozilla Firefox";
    String hub = "http://ec2-34-253-204-163.eu-west-1.compute.amazonaws.com:8070";

    public DataNtlmPassthru.Builder perform()
    {
        return new DataNtlmPassthru.Builder();
    }

    public class Builder
    {
        public Builder()
        {
            psCommand = String.format("psexec \\\\%s -u %s -p %s", tasProperties.getNtlmHost(), tasProperties.getNtlmSecurityPrincipal(),
                    tasProperties.getNtlmSecurityCredentials());
        }

        public Builder configureFirefox() throws Exception
        {
            STEP(String.format("[NTLM] Add alfresco ip address %s to network.automatic-ntlm-auth.trusted-uris", tasProperties.getTestServerUrl()));
            command = String.format("%s cmd /C \"cd \"%s\" && echo pref(\"network.automatic-ntlm-auth.trusted-uris\", \"%s\");pref(\"general.config.filename\", \"mozilla.cfg\");  > %s\"", psCommand, userPrefPath,
                    tasProperties.getTestServerUrl(), userPrefFile);
            STEP(Utility.executeOnWinAndReturnOutput(command));
            command = String.format("%s cmd /C \"cd \"%s\" && echo lockPref(\"browser.shell.checkDefaultBrowser\", false); > mozilla.cfg\"", psCommand, firefoxPath);
            STEP(Utility.executeOnWinAndReturnOutput(command));
            return this;
        }
    }

    private WebDriver setDriver() throws MalformedURLException
    {
        DesiredCapabilities cap = DesiredCapabilities.firefox();
        cap.setBrowserName("firefox");
        cap.setPlatform(Platform.WIN8);
        cap.setCapability("marionette", true);
        cap.setCapability("network.automatic-ntlm-auth.trusted-uris", tasProperties.getTestServerUrl());

        WebDriver driver = new RemoteWebDriver(new URL(String.format("%s/wd/hub", hub)), cap);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);

        return driver;
    }
    
    public String getTitleInFirefox(String url) throws MalformedURLException 
    {
        STEP(String.format("Open URL %s", url));
        WebDriver driver = setDriver();
        driver.get(url);
        String title = driver.getTitle();
        driver.quit();

        return title;
    }

    public String getAlertInFirefox(String url) throws MalformedURLException
    {
        WebDriver driver = setDriver();
        STEP(String.format("Open URL %s", url));
        driver.get(url);
        String alertText = driver.switchTo().alert().getText();
        driver.quit();

        return alertText;
    }
}
