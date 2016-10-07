package org.alfresco.utility.report;

import org.alfresco.utility.report.Bug;
import org.alfresco.utility.report.HtmlReportListener;
import org.junit.Assert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@ContextConfiguration("classpath:alfresco-tester-context.xml")
@Listeners(value = HtmlReportListener.class)
public class BugInHtmlReportTest  extends AbstractTestNGSpringContextTests
{
    @Test(groups="A")
    public void test1()
    {
        
    }
    
    @Test(groups="A")
    public void test2()
    {
        
    }
    
    @Bug(id="MNT-1")
    @Test
    public void test3()
    {   
       Assert.assertTrue(false);
    }
}
