package org.alfresco.utility.report;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
 *      @Listeners(value = ReportListenerAdapter.class)
 *        public class MyTestClass
 *       {
 *          ...   
 *       }
 * b) in your testNG suite file
 *       <listeners>
 *                <listener class-name="org.alfresco.tester.report.ReportListenerAdapter"></listener>
 *       </listeners>       
 * @author Paul Brodner
 */
public class ReportListenerAdapter implements IReporter
{
    private ExtentReports extent;

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory)
    {
        extent = ReportManager.getReporter();

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
}