package org.alfresco.utility.report.log;

import java.util.ArrayList;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class LogsTestNGListner implements ITestListener
{
    private static XmlLogWritter logWritter = new XmlLogWritter();
    
    @Override
    public void onTestStart(ITestResult result)
    {
        XmlLogWritter.testSteps = new ArrayList<String>();
        System.out.println("Test started running:" + result.getMethod().getMethodName() + " at:" + result.getStartMillis());
    }

    @Override
    public void onTestSuccess(ITestResult result)
    {
        System.out.println(result.getName() + " ----> success");
        
        for(String step : XmlLogWritter.testSteps)
        {
            System.out.println(step);
        }
        logWritter.addTestExecution(result, XmlLogWritter.testSteps);
    }

    @Override
    public void onTestFailure(ITestResult result)
    {
        System.out.println(result.getName() + " ----> fail");
        //System.out.println(testSteps.get(testSteps.size()-1) + " is fails");
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
        logWritter.setFinish(context);
    }
}
