package org.alfresco.utility.report.json;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(value = JsonReportListener.class)
public class JsonReportTest
{
    @Test(groups = { "a", "b" })
    public void testMethodOne()
    {
        Assert.assertTrue(true);
    }

    @Test(groups = "a")
    public void testMethodTwo()
    {
        Assert.assertTrue(true);
    }

    @Test()
    public void testMethodThree()
    {
        Assert.assertTrue(true);
    }
}