package org.alfresco.utility.testrail;

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
        Step.testSteps.clear();
        testCaseUploader.addTestRailIfNotExist(result);
    }

    @Override
    public void onTestSuccess(ITestResult result)
    {
        testCaseUploader.updateTestRailTestCase(result);
        testCaseUploader.updateTestRailTestSteps(result, String.join(System.lineSeparator(), Step.testSteps));
    }

    @Override
    public void onTestFailure(ITestResult result)
    {
        testCaseUploader.updateTestRailTestCase(result);
        testCaseUploader.updateTestRailTestSteps(result, String.join(System.lineSeparator(), Step.testSteps));
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
        testCaseUploader.updateTestRailTestSteps(result, String.join(System.lineSeparator(), Step.testSteps));
    }

    @Override
    public void onStart(ITestContext context)
    {
        testCaseUploader.oneTimeUpdateFromTestRail();
    }

    @Override
    public void onFinish(ITestContext context)
    {
        testCaseUploader.showTestCasesNotUploaded();
    }
}
