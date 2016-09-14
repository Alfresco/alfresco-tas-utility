package org.alfresco.utility.report;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
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
import org.alfresco.utility.exception.ReportConfigurationException;
import org.alfresco.utility.exception.TestConfigurationException;
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
public class ReportListenerAdapter implements IReporter
{
    static Logger LOG = LogFactory.getLogger();

    private ExtentReports extent;

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory)
    {
        try
        {
            extent = ReportManager.getReporter();
        }
        catch (TestConfigurationException | URISyntaxException e1)
        {
            LOG.error(e1.getMessage());
        }

        for (ISuite suite : suites)
        {
            Map<String, ISuiteResult> result = suite.getResults();

            for (ISuiteResult r : result.values())
            {
                ITestContext context = r.getTestContext();

                buildTestNodes(context.getPassedTests(), LogStatus.PASS);
                buildTestNodes(context.getFailedTests(), LogStatus.FAIL);
                buildTestNodes(context.getSkippedTests(), LogStatus.SKIP);
                buildTestNodes(context.getFailedConfigurations(), LogStatus.FATAL);
            }
        }

        String content = "";
        try
        {
            content = getLogsContent(getLogsLocation());
        }
        catch (ReportConfigurationException e)
        {
            LOG.error(e.getMessage());
        }

        extent.setTestRunnerOutput(String.format("<pre>%s </pre>", content));
        extent.flush();
        extent.close();
    }

    private void buildTestNodes(IResultMap tests, LogStatus status)
    {
        ExtentTest test;

        if (tests.size() > 0)
        {
            for (ITestResult result : tests.getAllResults())
            {
                test = extent.startTest(String.format("%s # %s", result.getInstance().getClass().getSimpleName(), result.getMethod().getMethodName()));

                test.setStartedTime(getTime(result.getStartMillis()));
                test.setEndedTime(getTime(result.getEndMillis()));

                for (String group : result.getMethod().getGroups())
                    test.assignCategory(group);

                if (result.getThrowable() != null)
                {
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

    private String getLogsContent(String filePath) throws ReportConfigurationException
    {
        String content = "";
        List<String> lines = new ArrayList<String>();

        try
        {
            BufferedReader reader = Files.newBufferedReader(Paths.get(filePath));
            lines = reader.lines().collect(Collectors.toList());
        }
        catch (IOException e)
        {
            throw new ReportConfigurationException(String.format("Cannot read log file due tos: %s", e.getMessage()));
        }

        for (String line : lines)
        {
            content = content.concat(line + "\n");
        }

        return content;
    }

    private String getLogsLocation()
    {
        String log4jPath = ".";
        Properties log4jProperties = new Properties();
        InputStream defaultProp = getClass().getClassLoader().getResourceAsStream("log4j.properties");
        if (defaultProp != null)
        {
            try
            {
                log4jProperties.load(defaultProp);
                log4jPath = log4jProperties.getProperty("log4j.appender.R.File");
            }
            catch (Exception e)
            {
                LOG.error("Cannot read properties from log4j.properties file");
            }
        }
        return log4jPath;
    }
}