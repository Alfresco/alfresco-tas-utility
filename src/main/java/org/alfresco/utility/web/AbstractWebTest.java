package org.alfresco.utility.web;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.alfresco.utility.LogFactory;
import org.alfresco.utility.web.browser.WebBrowser;
import org.alfresco.utility.web.browser.WebBrowserFactory;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

/**
 * Created by Claudia Agache on 3/9/2017.
 */
public abstract class AbstractWebTest extends AbstractTestNGSpringContextTests
{
    @Autowired
    WebBrowserFactory browserFactory;

    protected static final ThreadLocal<WebBrowser> browserThread = new ThreadLocal<WebBrowser>();

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

    @BeforeMethod(alwaysRun = true)
    public void startTest(final Method method) throws Exception {
        LOG.info("***************************************************************************************************");
        DateTime now = new DateTime();
        LOG.info("*** {} ***  Starting test {}:{} ", now.toString("HH:mm:ss"), method.getDeclaringClass().getSimpleName(), method.getName());
        LOG.info("***************************************************************************************************");
    }

    @AfterMethod(alwaysRun = true)
    public void endTest(final Method method, final ITestResult result) throws Exception {
        LOG.info("***************************************************************************************************");
        DateTime now = new DateTime();
        LOG.info("*** {} ***   Ending test {}:{} {} ({} s.)", now.toString("HH:mm:ss"), method.getDeclaringClass().getSimpleName(), method.getName(),
                result.isSuccess() ? "SUCCESS" : "!!! FAILURE !!!",
                (result.getEndMillis() - result.getStartMillis()) / 1000);
        LOG.info("***************************************************************************************************");
    }

    public static WebBrowser getBrowser()
    {

        return browserThread.get();
    }

    private void initializeBrowser() throws Exception
    {
        Field[] fields = this.getClass().getDeclaredFields();
        for(Field field : fields)
        {          
            field.setAccessible(true);
            Object pageObject = field.get(this);
            if(pageObject instanceof HtmlPage)
            {
                HtmlPage page = (HtmlPage) pageObject;
                page.setBrowser(getBrowser());
                field.set(this, page);
            }            
        }
    }    
}
