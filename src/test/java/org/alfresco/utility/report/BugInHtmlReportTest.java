package org.alfresco.utility.report;

import org.alfresco.utility.model.TestGroup;
import org.junit.Assert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@ContextConfiguration("classpath:alfresco-tester-context.xml")
@Listeners(value = HtmlReportListener.class)
public class BugInHtmlReportTest extends AbstractTestNGSpringContextTests
{

    @BeforeClass
    public void a()
    {
        Assert.assertFalse(true);
    }

    @Test(groups = "A")
    public void test1()
    {

    }
    
    @Test(groups = TestGroup.OS_WIN)
    public void test12()
    {

    }

    @Bug(id = "MNT-2", description="ddd")
    @Test(groups = "A")
    public void test2()
    {

    }

    @Bug(id = "MNT-1")
    @Test
    public void test3()
    {
        Assert.assertTrue(false);
    }

    @Test
    public void thsiShouldBeFiltered()
    {
        Assert.assertTrue(false);
    }

    @Test
    public void thisIsSkipped()
    {
        // throw new SkipException("hahaha");
    }
}
