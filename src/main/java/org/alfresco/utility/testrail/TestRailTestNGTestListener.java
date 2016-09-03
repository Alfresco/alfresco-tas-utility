package org.alfresco.utility.testrail;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Listen for all test cases created and update them accordingly
 */
public class TestRailTestNGTestListener implements ITestListener
{
    private static TestCaseUploader testCaseUploader = new TestCaseUploader();
  
    @Override
    public void onTestStart(ITestResult result)
    {
        System.out.println("onTestStart1");        
        testCaseUploader.addTestRailIfNotExist(result);
    }

    @Override
    public void onTestSuccess(ITestResult result)
    {        
        testCaseUploader.updateTestRailTestCase(result);
    }

    @Override
    public void onTestFailure(ITestResult result)
    {
        System.out.println("onTestFailure");

    }

    @Override
    public void onTestSkipped(ITestResult result)
    {
        System.out.println("onTestSkipped");

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result)
    {
        System.out.println("onTestFailedButWithSuccessPers");

    }

    @Override
    public void onStart(ITestContext context)
    {
        System.out.println("onStart");
    }

    @Override
    public void onFinish(ITestContext context)
    {
        System.out.println("onFinish");
        testCaseUploader.showTestCasesNotUploaded();
    }

}
