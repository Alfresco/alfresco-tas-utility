package org.alfresco.utility.testrail;

import java.util.ArrayList;
import java.util.List;

import org.alfresco.utility.Utility;
import org.alfresco.utility.report.log.Step;
import org.alfresco.utility.testrail.core.TestCaseDetail;
import org.alfresco.utility.testrail.core.TestRailExecutor;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Listen for all test cases created and update them accordingly in Test Rail (configured based on *.properties file)
 * This is the seconds approach of updating TestRail test cases based on test execution.
 * The initial approach was to perform queries and update test cases as we go. Running many tests in parallel or multiple suites will throw
 * "HTTP 429 (No additional error message received)" from the server {@link http://docs.gurock.com/testrail-api2/introduction}
 * -
 * In order to bypass this, before we start the suite of test we query the list of test cases from current project specified in default.properties and save it
 * in memory.
 * - when the test is executed if the test is not in temporary collection we add it (make a post request to /add_case)
 * - after test execution, for our test object we define the status and other details from ITestResult
 * - after the entire tests are executed, we traverse the list of all tests executed and bulk upload their results in TestRail with just one query command.
 * 
 * @author Paul Brodner
 */
public class TestRailExecutorListener implements ISuiteListener, ITestListener
{
    private static TestRailExecutor testRailExecutor = new TestRailExecutor();
    private static List<TestCaseDetail> currentTestCases = new ArrayList<TestCaseDetail>();

    @Override
    public void onStart(ISuite suite)
    {
        if (testRailExecutor.isEnabled())
        {
            /*
             * get the list of sections
             * get the current run object
             * get list of all test cases
             */
            testRailExecutor.prepareCurrentSuiteRun();
        }
        else
        {
            TestRailExecutor.LOG.info(
                    "'TestRailExecutorListener' is added in your suite.xml file, but the property: testManagement.enabled is set to 'false' in your {} file",
                    Utility.getEnvironmentPropertyFile());
        }
    }

    @Override
    public void onFinish(ISuite suite)
    {
        try
        {
            testRailExecutor.addResultsForCases(currentTestCases);
        }
        catch (Exception e)
        {
            TestRailExecutor.LOG.error("CANNOT UPDATE TestCases in TestRail: {}", e.getMessage());
        }
    }

    @Override
    public void onTestStart(ITestResult currentTest)
    {
        Step.testSteps.clear();
    }

    @Override
    public void onTestSuccess(ITestResult result)
    {
        if (testRailExecutor.isEnabled())
        {
            currentTestCases.add(testRailExecutor.uploadTestCase(result));
        }
    }

    @Override
    public void onTestFailure(ITestResult result)
    {
        if (testRailExecutor.isEnabled())
        {
            currentTestCases.add(testRailExecutor.uploadTestCase(result));
        }
    }

    @Override
    public void onTestSkipped(ITestResult result)
    {
        if (testRailExecutor.isEnabled())
        {
            currentTestCases.add(testRailExecutor.uploadTestCase(result));
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result)
    {
        // nothing to do here
    }

    @Override
    public void onStart(ITestContext context)
    {
        Step.testSteps.clear();
    }

    @Override
    public void onFinish(ITestContext context)
    {
        // nothing to do here
    }
}
