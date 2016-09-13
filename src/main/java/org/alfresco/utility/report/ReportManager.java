package org.alfresco.utility.report;

import java.util.Properties;

import org.alfresco.utility.LogFactory;
import org.alfresco.utility.Utility;
import org.alfresco.utility.exception.TestConfigurationException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.relevantcodes.extentreports.ExtentReports;

/**
 * Handling ExtentReports
 * 
 * @author Paul Brodner
 */
@Service
public class ReportManager
{
    static Logger LOG = LogFactory.getLogger();

    static ExtentReports extent;
    final static String REPORT_PATH = "report.html";

    public synchronized static ExtentReports getReporter() throws TestConfigurationException
    {
        if (extent == null)
        {
            Properties properties = Utility.getProperties(ReportManager.class, "default.properties");

            LOG.info("Using ReportManager to generate HTML report on {}", REPORT_PATH);
            extent = new ExtentReports(REPORT_PATH, true);
            extent.loadConfig(FileUtils.getFile("src", "test", "resources", "alfresco-report-config.xml"));
            extent.addSystemInfo("Alfresco Server", String.format("%s://%s:%s", 
                    properties.getProperty("alfresco.scheme"), 
                    properties.getProperty("alfresco.server"),
                    properties.getProperty("alfresco.port")));
        }
        return extent;
    }
}
