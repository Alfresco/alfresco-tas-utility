package org.alfresco.utility.report.json;

import org.alfresco.utility.LogFactory;
import org.alfresco.utility.Utility;
import org.alfresco.utility.exception.TestConfigurationException;
import org.slf4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class JsonReportListener implements ITestListener
{
    protected Logger LOG = LogFactory.getLogger();

    JsonSuite suite;
    String jsonPathFile = null;

    @Override
    public void onTestStart(ITestResult result)
    {
    }

    @Override
    public void onTestSuccess(ITestResult result)
    {
        suite.getTestClass().addTest(result);
    }

    @Override
    public void onTestFailure(ITestResult result)
    {
        suite.getTestClass().addTest(result);
    }

    @Override
    public void onTestSkipped(ITestResult result)
    {
        suite.getTestClass().addTest(result);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result)
    {
        suite.getTestClass().addTest(result);
    }

    @Override
    public void onStart(ITestContext context)
    {
        if (jsonPathFile == null)
            try
            {
                jsonPathFile = Utility.getProperties(getClass(), Utility.getEnvironmentPropertyFile()).getProperty("reports.path");
            }
            catch (TestConfigurationException e)
            {
                LOG.error("CANNOT Read environment properties file for reports.path: {} ", e.getMessage());
            }

        suite = new JsonSuite(context, jsonPathFile);
    }

    @Override
    public void onFinish(ITestContext context)
    {
        try
        {
            suite.setFinishedAt(context.getEndDate().toString());
            suite.writeToDisk();
        }
        catch (Exception e)
        {
            LOG.error("CANNOT GENERATE JSON Test File: {} ", e.getMessage());
        }
    }

}
