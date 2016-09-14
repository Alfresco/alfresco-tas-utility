package org.alfresco.utility.report.log;

import java.nio.file.Paths;

import org.alfresco.utility.report.ReportListenerAdapter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@ContextConfiguration("classpath:alfresco-tester-context.xml")
@Listeners(value = ReportListenerAdapter.class)
public class XmlLogWritterTest extends AbstractTestNGSpringContextTests
{
    @Test
    public void checkXMLIsInitialized()
    {
        new XmlLogWritter();

        String[] files = {"./target/reports", 
                          "./target/reports/logs/TransformLog.xsl", 
                          "./target/reports/logs/logo.png"
                          };
        
        for(String file : files)
        {
            Assert.assertTrue(Paths.get(file).toFile().exists(), file + " file is generated.");    
        }        
    }
}
