package org.alfresco.utility.testrail;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.alfresco.utility.Utility;
import org.alfresco.utility.exception.TestConfigurationException;
import org.alfresco.utility.report.log.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Listen for all test cases created and update them accordingly
 */
public class TestRailExecutorListener implements ITestListener
{
    static Logger LOG = LoggerFactory.getLogger("testrail");
    private static boolean isTestRailExecutorEnabled = isPropertyEnabled("testManagement.enabled");
    private static boolean justUpdateResults = isPropertyEnabled("testManagement.updateTestExecutionResultsOnly");    
    private static TestCaseUploader testCaseUploader = new TestCaseUploader();

    @Override
    public void onTestStart(ITestResult result)
    {
        if (!isTestRailExecutorEnabled)
        {
            LOG.info("'TestRailExecutorListener' is added in your suite.xml file, but the property: testManagement.enabled is set to 'false' in your {} file",
                    Utility.getEnvironmentPropertyFile());
            return;
        }
        
        if (justUpdateResults)
        {
            LOG.info("'testManagement.updateTestExecutionResultsOnly' is set to 'true' in your {} file, so only the test execution status will be updated in TestRail.", Utility.getEnvironmentPropertyFile());
        }

        testCaseUploader.addTestRailIfNotExist(result);
    }

    @Override
    public void onTestSuccess(ITestResult result)
    {
        if (!isTestRailExecutorEnabled)
            return;
        
        testCaseUploader.updateTestRailTestCase(result);

        // no need to update the content of the test case, we wanted just the results as updated above
        if (justUpdateResults)
            return;

        List<String> stepsToBeUpdated = new ArrayList<String>();

        if (Step.testSteps.get(result.getTestClass().getName()) != null)
        {
            stepsToBeUpdated.add("---START DATAPREP -----");
            stepsToBeUpdated.addAll(Step.testSteps.get(result.getTestClass().getName()));
            stepsToBeUpdated.add("---END DATAPREP -----");

        }
        if (Step.testSteps.get(result.getMethod().getMethodName()) != null)
        {
            stepsToBeUpdated.addAll(Step.testSteps.get(result.getMethod().getMethodName()));

        }
        testCaseUploader.updateTestRailTestSteps(result, String.join(System.lineSeparator(), stepsToBeUpdated));
    }

    @Override
    public void onTestFailure(ITestResult result)
    {
        if (!isTestRailExecutorEnabled)
            return;
        testCaseUploader.updateTestRailTestCase(result);

        // no need to update the content of the test case, we wanted just the results as updated above
        if (justUpdateResults)
            return;

        List<String> stepsToBeUpdated = new ArrayList<String>();

        if (Step.testSteps.get(result.getTestClass().getName()) != null)
        {
            stepsToBeUpdated.add("---START DATAPREP -----");
            stepsToBeUpdated.addAll(Step.testSteps.get(result.getTestClass().getName()));
            stepsToBeUpdated.add("---END DATAPREP -----");

        }
        if (Step.testSteps.get(result.getMethod().getMethodName()) != null)
        {
            stepsToBeUpdated.addAll(Step.testSteps.get(result.getMethod().getMethodName()));
        }
        testCaseUploader.updateTestRailTestSteps(result, String.join(System.lineSeparator(), stepsToBeUpdated));
    }

    @Override
    public void onTestSkipped(ITestResult result)
    {
        if (!isTestRailExecutorEnabled)
            return;
        testCaseUploader.updateTestRailTestCase(result);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result)
    {
        if (!isTestRailExecutorEnabled)
            return;
        testCaseUploader.updateTestRailTestCase(result);

        // no need to update the content of the test case, we wanted just the results as updated above
        if (justUpdateResults)
            return;

        List<String> stepsToBeUpdated = new ArrayList<String>();

        if (Step.testSteps.get(result.getTestClass().getName()) != null)
        {
            stepsToBeUpdated.add("---START DATAPREP -----");
            stepsToBeUpdated.addAll(Step.testSteps.get(result.getTestClass().getName()));
            stepsToBeUpdated.add("---END DATAPREP -----");

        }
        if (Step.testSteps.get(result.getMethod().getMethodName()) != null)
        {
            stepsToBeUpdated.addAll(Step.testSteps.get(result.getMethod().getMethodName()));
        }
        testCaseUploader.updateTestRailTestSteps(result, String.join(System.lineSeparator(), stepsToBeUpdated));
    }

    @Override
    public void onStart(ITestContext context)
    {
        if (!isTestRailExecutorEnabled)
            return;
        testCaseUploader.oneTimeUpdateFromTestRail();

        // no need to update the content of the test case, we wanted just the results as updated above
        if (justUpdateResults)
            return;

        Step.testSteps.clear();

    }

    @Override
    public void onFinish(ITestContext context)
    {
        if (!isTestRailExecutorEnabled)
            return;
        testCaseUploader.showTestCasesNotUploaded();
    }

    private static boolean isPropertyEnabled(String propertyName)
    {
        boolean isEnabled = false;
        Properties properties = new Properties();
        try
        {
            properties = Utility.getProperties(TestRailExecutorListener.class, Utility.getEnvironmentPropertyFile());
            isEnabled = Boolean.valueOf(Utility.getSystemOrFileProperty(propertyName, properties));
        }
        catch (TestConfigurationException e1)
        {
            System.err.println("Cannot read properties from '" + Utility.getEnvironmentPropertyFile() + "'. Error: " + e1.getMessage());
        }

        return isEnabled;
    }
}
