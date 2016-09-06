package org.alfresco.utility.report.log;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class LogsTestNGListner implements ITestListener 
{
    private static XmlLogWritter logWritter = new XmlLogWritter();
    
    @Override
    public void onTestStart(ITestResult result)
    {
        System.out.println("Test started running:"  + result.getMethod().getMethodName() + " at:" + result.getStartMillis());
        
    }

    @Override
    public void onTestSuccess(ITestResult result)
    {
        System.out.println(result.getName() + " ----> success");
        
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
        System.out.println("<<<<<< On Start context name:" + context.getName());
        logWritter.generateXmlFile(context);
    }

    @Override
    public void onFinish(ITestContext context)
    {
        System.out.println("Passed: " + context.getPassedTests());
        System.out.println("Failled:" + context.getFailedTests()); 
        System.out.println("Skiped:" + context.getSkippedTests()); 
    }
}
