package org.alfresco.utility.web;

import org.alfresco.utility.LogFactory;
import org.alfresco.utility.web.browser.WebBrowser;
import org.alfresco.utility.web.browser.WebBrowserFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

/**
 * Created by Claudia Agache on 3/9/2017.
 */
public abstract class AbstractWebTest extends AbstractTestNGSpringContextTests
{
    @Autowired
    WebBrowserFactory browserFactory;

    private static final ThreadLocal<WebBrowser> browserThread = new ThreadLocal<WebBrowser>();

    protected Logger LOG = LogFactory.getLogger();

    @BeforeSuite(alwaysRun = true)
    public void initializeBeans() throws Exception
    {
        super.springTestContextPrepareTestInstance();
    }

    @BeforeClass(alwaysRun = true)
    public void defineBrowser() throws Exception
    {
        browserThread.set(browserFactory.getWebBrowser());

        initializeBrowser();
    }

    @AfterClass(alwaysRun = true)
    public void closeBrowser()
    {
        LOG.info("Closing WebDrone!");
        if (getBrowser() != null)
        {
            getBrowser().quit();
        }
    }

    public static WebBrowser getBrowser()
    {
        return browserThread.get();
    }

    abstract void initializeBrowser();
}
