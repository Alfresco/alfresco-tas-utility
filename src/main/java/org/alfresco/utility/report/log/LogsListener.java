package org.alfresco.utility.report.log;

import java.util.ArrayList;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class LogsListener implements ITestListener
{
    private static XmlLogWritter logWritter = new XmlLogWritter();
    
    @Override
    public void onTestStart(ITestResult result)
    {
        XmlLogWritter.testSteps = new ArrayList<String>();
    }

    @Override
    public void onTestSuccess(ITestResult result)
    {
        logWritter.addTestExecution(result, XmlLogWritter.testSteps);
    }

    @Override
    public void onTestFailure(ITestResult result)
    {
        System.out.println(result.getName() + " ----> fail");
    }

    @Override
    public void onTestSkipped(ITestResult result)
    {
        System.out.println(result.getName() + " ----> fail");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result)
    {
        // TODO Auto-generated method stub

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
