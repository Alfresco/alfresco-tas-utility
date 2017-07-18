package org.alfresco.utility.report;

import java.io.File;
import java.nio.file.Paths;
import java.util.Properties;

import org.alfresco.utility.LogFactory;
import org.alfresco.utility.Utility;
import org.alfresco.utility.exception.TestConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.sikuli.script.FindFailed;
import org.slf4j.Logger;
import org.testng.ITestResult;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class GuiScreenImageHandler
{
    static Logger LOG = LogFactory.getLogger();

    private ITestResult testResult;
    protected LogStatus status;
    protected Properties properties;

    public GuiScreenImageHandler(ITestResult testResult, LogStatus status) throws TestConfigurationException
    {
        properties = Utility.getProperties(HtmlReportListener.class, Utility.getEnvironmentPropertyFile());
        setTestResult(testResult);
        this.status = status;
    }

    public ITestResult getTestResult()
    {
        return testResult;
    }

    public void setTestResult(ITestResult testResult)
    {
        this.testResult = testResult;
    }

    public static void addGuiScreenImageFrom(ITestResult testResult, ExtentTest test, LogStatus status)
    {
        try
        {
            GuiScreenImageHandler handler = new GuiScreenImageHandler(testResult, status);
            handler.addImageTo(test);
        }
        catch (TestConfigurationException e)
        {
            e.printStackTrace();
            LOG.error("Cannot handle Gui Screen errors: {}", e.getMessage());
        }
    }

    public String getReportPath()
    {
        return properties.getProperty("reports.path");
    }

    public File getScreenshotReport()
    {
        return Paths.get(getReportPath(), properties.getProperty("screenshots.dir")).toFile();
    }

    private void addImageTo(ExtentTest test)
    {
        if (getTestResult().getThrowable() instanceof FindFailed)
        {
            
            FindFailed missingImage = (FindFailed) getTestResult().getThrowable();
            try
            {
                String missingImagePath = missingImage.getMessage().split(".png:")[0];
                File source = Paths.get(String.format("%s.png", missingImagePath)).toFile();
                File destination = Paths.get(getScreenshotReport().getPath(), source.getPath().split("target/classes")[1]).toFile();
                String destinationFileInHtml = destination.getPath().split(getReportPath())[1];
                if(destinationFileInHtml.startsWith("/"))
                {
                    destinationFileInHtml = StringUtils.removeFirst(destinationFileInHtml, "/");
                }
                System.out.println(source.getPath());
                System.out.println(destination.getPath());
                System.out.println(destination.getPath());

                FileUtils.copyFile(source, destination);
                test.log(status, String.format("The image that you want to search for, was not found on screen: %s", test.addScreenCapture(destinationFileInHtml)));
            }
            catch (Exception e)
            {
                LOG.error("CANNOT CAPTURE IMAGE in HtmlReport: {}", e.getMessage());
            }
            
        }
    }
}
