package org.alfresco.utility.web;

import org.alfresco.utility.LogFactory;
import org.alfresco.utility.web.browser.WebBrowser;
import org.alfresco.utility.web.browser.WebBrowserFactory;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.Set;

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

    private void initializeBrowser() throws ClassNotFoundException
    {
        /* get all autowired annotated children of this class */
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(true);
        provider.addIncludeFilter(new AssignableTypeFilter(this.getClass()));

        Set<BeanDefinition> components = provider.findCandidateComponents(getPageObjectRootPackage());
        for (BeanDefinition component : components)
        {
            @SuppressWarnings("rawtypes")
            Class pageObject = Class.forName(component.getBeanClassName());

            //System.out.println("Page Object: " + pageObject.getName());
            /*
             * only for HtmlPage base classes
             */
            if (pageObject.getClass().isInstance(HtmlPage.class))
            {
                @SuppressWarnings("unchecked")
                Object bean = applicationContext.getBean(pageObject);
                if (bean instanceof HtmlPage)
                {
                    HtmlPage page = (HtmlPage) bean;
                    page.setBrowser(getBrowser());
                }
            }

        }
    }

    public abstract String getPageObjectRootPackage();
}
