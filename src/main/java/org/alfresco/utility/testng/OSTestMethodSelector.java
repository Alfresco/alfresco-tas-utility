package org.alfresco.utility.testng;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.alfresco.utility.Utility;
import org.alfresco.utility.exception.TestConfigurationException;
import org.alfresco.utility.model.TestGroup;
import org.alfresco.utility.report.Bug;
import org.alfresco.utility.web.AbstractWebTest;
import org.alfresco.utility.web.browser.WebBrowser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.testng.internal.ConstructorOrMethod;

/**
 * Use this listener if you want to exclude automatically tests based on your operating system.
 * Example: <code>
 * &#64;Test(groups = { TestGroup.OS_WIN })
 * public void testA()
 * {
 * }
 * 
 * &#64;Test(groups = { TestGroup.OS_WIN, TestGroup.OS_UNIX })
 * public void testB()
 * {
 * }
 * 
 * &#64;Test(groups = { TestGroup.OS_WIN })
 * public void testC()
 * {
 * }
 * 
 * and you are running your tests on Unix, then only testB will be executed, the remaining ones will be marked as skipped.
 * 
 * </code>
 * 
 * @author Paul Brodner
 */
public class OSTestMethodSelector implements IInvokedMethodListener
{
    private WebBrowser browser;
    private static final Logger LOG = LoggerFactory.getLogger(OSTestMethodSelector.class);
    static Properties defaultProperties;

    @Override
    public void beforeInvocation(IInvokedMethod testNGmethod, ITestResult testResult)
    {
        ConstructorOrMethod contructorOrMethod = testNGmethod.getTestMethod().getConstructorOrMethod();
        Method method = contructorOrMethod.getMethod();
        if (method != null)
        {
            if (method.isAnnotationPresent(Test.class))
            {
                Test testClass = method.getAnnotation(Test.class);

                String runBugs = System.getProperty("runBugs");
                if ("false".equals(runBugs))
                {
                    if (method.isAnnotationPresent(Bug.class))
                    {
                        Bug bug = method.getAnnotation(Bug.class);
                        switch (bug.status())
                        {
                            case FIXED:
                                break;
                            case OPENED:
                                throw new SkipException(
                                        String.format(
                                                "This test is skipped because it is marked as OPENED BUG: {[id='%s', description='%s', status='%s']}.(info: you can run tests marked as opened bugs, passing -DrunBugs=true)",
                                                bug.id(), bug.description(), bug.status()));
                            default:
                                throw new IllegalArgumentException();
                        }

                    }
                }

                List<String> groups = Arrays.asList(testClass.groups());

                if (groups != null)
                {
                    if (groups.contains(TestGroup.OS_LINUX) || groups.contains(TestGroup.OS_WIN) || groups.contains(TestGroup.OS_MAC))
                    {
                        if (SystemUtils.IS_OS_LINUX && !groups.contains(TestGroup.OS_LINUX))
                        {
                            throw new SkipException(
                                    String.format(
                                            "This test was skipped because it was marked to be executed on differed operating system(s). Groups used: %s and was executed on: %s",
                                            String.valueOf(groups), System.getProperty("os.name")));
                        }

                        else if (SystemUtils.IS_OS_MAC && !groups.contains(TestGroup.OS_MAC))
                        {
                            throw new SkipException(
                                    String.format(
                                            "This test was skipped because it was marked to be executed on differed operating system(s). Groups used: %s and was executed on: %s",
                                            String.valueOf(groups), System.getProperty("os.name")));
                        }

                        else if (SystemUtils.IS_OS_WINDOWS && !groups.contains(TestGroup.OS_WIN))
                        {
                            throw new SkipException(
                                    String.format(
                                            "This test was skipped because it was marked to be executed on differed operating system(s). Groups used: %s and was executed on: %s",
                                            String.valueOf(groups), System.getProperty("os.name")));
                        }

                    }
                }
            }
        }

    }

    @Override
    public void afterInvocation(IInvokedMethod testNGmethod, ITestResult testResult)
    {
        if (!testResult.isSuccess())
        {
            if (testResult.getInstance() instanceof AbstractWebTest)
            {
                this.browser = ((AbstractWebTest) testResult.getInstance()).getBrowser();

                File screenshot = ((TakesScreenshot) browser).getScreenshotAs(OutputType.FILE);

                try
                {
                    defaultProperties = Utility.getProperties(OSTestMethodSelector.class, Utility.getEnvironmentPropertyFile());
                }
                catch (TestConfigurationException e)
                {
                    LOG.error("Could not get default properties: " + e.getMessage());
                }

                String screenshotsPath = defaultProperties.getProperty("reports.path") + File.separator + defaultProperties.getProperty("screenshots.dir");
                File saved = new File(screenshotsPath, testResult.getMethod().getMethodName() + ".png");
                try
                {
                    FileUtils.copyFile(screenshot, saved);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

}
