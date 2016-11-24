package org.alfresco.utility.testrail;

import java.util.ArrayList;
import java.util.List;

import org.alfresco.utility.report.log.Step;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Listen for all test cases created and update them accordingly
 */
public class TestRailExecutorListener implements ITestListener
{
    private static TestCaseUploader testCaseUploader = new TestCaseUploader();

    @Override
    public void onTestStart(ITestResult result)
    {

        testCaseUploader.addTestRailIfNotExist(result);
    }

    @Override
    public void onTestSuccess(ITestResult result)
    {
        testCaseUploader.updateTestRailTestCase(result);
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
        testCaseUploader.updateTestRailTestCase(result);
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
        testCaseUploader.updateTestRailTestCase(result);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result)
    {
        testCaseUploader.updateTestRailTestCase(result);
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
        Step.testSteps.clear();
        testCaseUploader.oneTimeUpdateFromTestRail();
    }

    @Override
    public void onFinish(ITestContext context)
    {
        testCaseUploader.showTestCasesNotUploaded();
    }
}
