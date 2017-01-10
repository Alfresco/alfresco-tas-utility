package org.alfresco.utility.report;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Properties;

import org.alfresco.utility.LogFactory;
import org.alfresco.utility.Utility;
import org.alfresco.utility.exception.TestConfigurationException;
import org.apache.commons.lang3.StringUtils;
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

    public synchronized static ExtentReports getReporter() throws TestConfigurationException, URISyntaxException
    {
        if (extent == null)
        {
            Properties properties = Utility.getProperties(ReportManager.class, Utility.getEnvironmentPropertyFile());
            String reportHtmlPath = properties.getProperty("reports.path");
            if (StringUtils.isEmpty(reportHtmlPath))
                reportHtmlPath = "./target/reports";
            reportHtmlPath = Paths.get(reportHtmlPath, "report.html").toFile().getPath();

            extent = new ExtentReports(reportHtmlPath, true);
            LOG.info("Initializing ReportManager to generate HTML report at:{}", reportHtmlPath);

            URL reportConfigUrl = ReportManager.class.getClassLoader().getResource("shared-resources/report/alfresco-report-config.xml");

            try
            {
                extent.loadConfig(Paths.get(reportConfigUrl.toURI()).toFile());
                LOG.info("Loaded ReportManager configuration file :{}", reportHtmlPath);
            }
            catch (Exception e)
            {
                LOG.error("Loaded ReportManager configuration file: {}. Error: {}", reportConfigUrl, e.getMessage());
            }

            extent.addSystemInfo("Alfresco Server", 
                    String.format("%s://%s:%s", 
                            Utility.getSystemOrFileProperty("alfresco.scheme",properties), 
                            Utility.getSystemOrFileProperty("alfresco.server",properties),
                            Utility.getSystemOrFileProperty("alfresco.port",properties)));
        }
        return extent;
    }
}
