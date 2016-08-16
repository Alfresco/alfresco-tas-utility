package org.alfresco.utility.report;

import org.alfresco.utility.LogFactory;
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
    final static String EXTENT_CONFIGURATION_FILE_NAME = "alfresco-report-config.xml";

    public synchronized static ExtentReports getReporter()
    {
        if (extent == null)
        {
            LOG.info("Using ReportManager to generate HTML report on {}", REPORT_PATH);
            extent = new ExtentReports(REPORT_PATH, true);
            //extent.loadConfig(FileUtils.getFile("src", "test", "resources", EXTENT_CONFIGURATION_FILE_NAME));
        }
        return extent;
    }
}
