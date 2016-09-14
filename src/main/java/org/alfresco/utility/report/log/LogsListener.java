package org.alfresco.utility.report.log;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class LogsListener implements ITestListener
{
    private static XmlLogWritter logWritter = new XmlLogWritter();

    @Override
    public void onTestStart(ITestResult result)
    {
        XmlLogWritter.LOG.info("Starting test: " + result.getMethod().getMethodName());
        Step.testSteps.clear();
    }

    @Override
    public void onTestSuccess(ITestResult result)
    {
        logWritter.addTestExecution(result, Step.testSteps);
    }

    @Override
    public void onTestFailure(ITestResult result)
    {
        logWritter.addTestExecution(result, Step.testSteps);
    }

    @Override
    public void onTestSkipped(ITestResult result)
    {
        logWritter.addTestExecution(result, Step.testSteps);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result)
    {
        logWritter.addTestExecution(result, Step.testSteps);
    }

    @Override
    public void onStart(ITestContext context)
    {
        logWritter.generateXmlFile(context);
    }

    @Override
    public void onFinish(ITestContext context)
    {
        logWritter.setFinish(context);
    }
}
