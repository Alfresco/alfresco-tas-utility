package org.alfresco.utility.report;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.alfresco.utility.LogFactory;
import org.alfresco.utility.Utility;
import org.alfresco.utility.application.gui.AbstractGuiTest;
import org.alfresco.utility.exception.CouldNotFindImageOnScreen;
import org.alfresco.utility.exception.TestConfigurationException;
import org.alfresco.utility.report.Bug.Status;
import org.alfresco.utility.web.AbstractWebTest;
import org.sikuli.script.FindFailed;
import org.slf4j.Logger;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Enables ReportManager to generate custom HTML reports of failed/skipped/passed tests
 * How to use it
 * a) one approach is to add this listener to your test class
 * Example:
 * 
 * @Listeners(value = ReportListenerAdapter.class)
 *                  public class MyTestClass
 *                  {
 *                  ...
 *                  }
 *                  b) in your testNG suite file
 *                  <listeners>
 *                  <listener class-name="org.alfresco.tester.report.ReportListenerAdapter"></listener>
 *                  </listeners>
 * @author Paul Brodner
 */
public class HtmlReportListener implements IReporter
{
    static Logger LOG = LogFactory.getLogger();
    static Properties log4jProperties;
    static Properties defaultProperties;
    private ExtentReports extent = null;

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory)
    {
        try
        {
            log4jProperties = Utility.getProperties(HtmlReportListener.class, "log4j.properties");
            defaultProperties = Utility.getProperties(HtmlReportListener.class, Utility.getEnvironmentPropertyFile());
            extent = ReportManager.getReporter();
        }
        catch (TestConfigurationException | URISyntaxException e1)
        {
            LOG.error("Could not initialize HTML Report: " + e1.getMessage());
            return;
        }

        for (ISuite suite : suites)
        {
            Map<String, ISuiteResult> result = suite.getResults();

            for (ISuiteResult r : result.values())
            {
                ITestContext context = r.getTestContext();
                // buildTestNodes(context.getSkippedConfigurations(), LogStatus.FATAL);
                buildTestNodes(context.getFailedConfigurations(), LogStatus.FATAL);
                buildTestNodes(context.getPassedTests(), LogStatus.PASS);
                buildTestNodes(context.getFailedTests(), LogStatus.FAIL);
                buildTestNodes(context.getSkippedTests(), LogStatus.SKIP);
            }
        }

        if (defaultProperties.getProperty("testManagement.enabled").equals("true"))
        {
            String contentTestRail = getLogsContent(getLogsLocation("log4j.appender.testrailLog.File", log4jProperties));
            extent.setTestRunnerOutput(String.format("<pre>%s </pre>", contentTestRail));
        }

        String content = getLogsContent(getLogsLocation("log4j.appender.file.File", log4jProperties));
        extent.setTestRunnerOutput(String.format("<pre>%s </pre>", content));

        try
        {
            extent.flush();
            extent.close();
        }
        catch (Exception e)
        {
            LOG.error("Something happened on closing the report: {}", e.getMessage());
        }
    }

    private String trackerUrl(String issueID)
    {
        return String.format("<a href=\"https://issues.alfresco.com/jira/browse/%s\" target=\"_blank\">%s</a>", issueID, issueID);
    }

    private void buildTestNodes(IResultMap tests, LogStatus status)
    {
        if (extent == null)
            return;

        ExtentTest test;

        if (tests.size() > 0)
        {
            for (ITestResult result : tests.getAllResults())
            {
                /**
                 * BUG section, taking in consideration TestNG tests that are marked with @Bug annotation
                 */
                Bug bugAnnotated = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Bug.class);
                String testName = "";
                Object[] objs = result.getParameters();
                if (objs.length > 0)
                {
                    // test has @DataProvider
                    testName = objs[0].toString();
                }
                else
                {
                    testName = result.getMethod().getMethodName();
                }

                if (bugAnnotated != null)
                {
                    test = extent.startTest(
                            String.format("%s # %s (BUG: %s)", result.getInstance().getClass().getSimpleName(), testName, trackerUrl(bugAnnotated.id())));
                    if (bugAnnotated.status().equals(Status.OPENED))
                    {
                        test.assignCategory("BUGS");
                        if (bugAnnotated.description().isEmpty() && status != LogStatus.SKIP)
                        {
                            test.log(status, String.format("This test is failing due to this issue %s", trackerUrl(bugAnnotated.id())));
                        }
                        else if (!bugAnnotated.description().isEmpty() && status != LogStatus.SKIP)
                        {
                            test.log(status, String.format("This test is failing due to this issue %s. <b>Description:</b> %s", trackerUrl(bugAnnotated.id()),
                                    bugAnnotated.description()));
                        }
                        if (status == LogStatus.PASS)
                        {
                            test.log(status,
                                    String.format(
                                            "Currently, test with opened bug %s is passed. Please check if this issue is passed and update the @Bug status to FIXED.",
                                            trackerUrl(bugAnnotated.id())));
                        }
                    }
                    else
                    {
                        if (status == LogStatus.PASS)
                        {
                            test.assignCategory("FIXED-BUGS");
                            test.log(status,
                                    String.format("Currently, test passed. But it failed in a regression due to this issue %s", trackerUrl(bugAnnotated.id())));
                        }
                    }

                }
                else
                {
                    test = extent.startTest(String.format("%s # %s", result.getInstance().getClass().getSimpleName(), testName));
                    test.assignCategory("WITHOUT-BUGS");
                }

                if (status == LogStatus.SKIP && result.getTestContext().getFailedConfigurations().size() > 0)
                {
                    test.log(status,
                            "Test is skipped due to a configuration test method like a @BeforeClass method. Filter the tests by 'FATAL' error to analyze the root cause.");
                }

                test.setStartedTime(getTime(result.getStartMillis()));
                test.setEndedTime(getTime(result.getEndMillis()));

                for (String group : result.getMethod().getGroups())
                    test.assignCategory(group);

                if (result.getThrowable() != null)
                {
                    if (result.getInstance() instanceof AbstractWebTest)
                    {
                        String screenshotsDir = defaultProperties.getProperty("screenshots.dir");
                        String screenshotsPath = Paths.get(defaultProperties.getProperty("reports.path"), screenshotsDir).toString();
                        File screenshot = Paths.get(screenshotsPath, testName + ".png").toFile();
                        if (screenshot.exists())
                            test.log(status, String.format("Screenshot below: %s",
                                    test.addScreenCapture(Paths.get(screenshotsDir, testName + ".png").toFile().getPath())));
                    }

                    if (result.getInstance() instanceof AbstractGuiTest)
                    {

                        if (result.getThrowable() instanceof CouldNotFindImageOnScreen)
                        {
                            CouldNotFindImageOnScreen missingImage = (CouldNotFindImageOnScreen) result.getThrowable();

                            test.log(status, String.format("GUI Image NOT found on screen: %s", test.addScreenCapture(missingImage.getImagePath())));

                        } else  if (result.getThrowable() instanceof FindFailed)
                        {
                            FindFailed missingImage = (FindFailed) result.getThrowable();
                             
                            String[] imageParsed = missingImage.getMessage().split(":"); 
                            if (imageParsed.length>0)
                            {
                                test.log(status, String.format("GUI Image NOT found on screen: %s", test.addScreenCapture(imageParsed[0])));                                    
                            }                            
                        }
                    }
                    
                    // also log the errors
                    test.log(status, result.getThrowable());
                }
                else
                {
                    test.log(status, "Test " + status.toString().toLowerCase() + "ed");
                }
                extent.endTest(test);
            }
        }
    }

    private Date getTime(long millis)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }

    private String getLogsContent(String filePath)
    {
        String content = "";
        try
        {
            List<String> lines = new ArrayList<String>();
            BufferedReader reader = null;

            reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.ISO_8859_1);
            lines = reader.lines().collect(Collectors.toList());
            content = String.join("\n", lines);
        }
        catch (IOException e)
        {
            LOG.error(String.format("Cannot read log file due tos: %s", e.getMessage()));
        }
        return content;
    }

    private String getLogsLocation(String key, Properties properties)
    {
        String log4jPath = ".";
        Properties log4jProperties = new Properties();
        try
        {
            log4jProperties = Utility.getProperties(getClass(), "log4j.properties");
            log4jPath = log4jProperties.getProperty(key);
        }
        catch (TestConfigurationException e1)
        {
            LOG.error("Cannot read '{}' key from log4j.properties file. Error: {}", key, e1.getMessage());
        }
        return log4jPath;
    }
}