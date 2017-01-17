package org.alfresco.utility.testrail;

import org.alfresco.utility.Utility;
import org.alfresco.utility.report.Bug;
import org.alfresco.utility.testrail.annotation.TestRail;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@ContextConfiguration("classpath:alfresco-tester-context.xml")
@Listeners(value = TestRailExecutorListener.class)
public class RunTestRailIntegrationTest extends AbstractTestNGSpringContextTests
{
    @Test(groups = "sample-tests")
    @TestRail(section = { "demo", "sample-section1" }, description = "My Awesome test 100", executionType = ExecutionType.SMOKE)
    public void ThisIsSomethingNew2()
    {
    }

    @Test(groups = "sample-tests")
    @TestRail(section = { "demo", "sample-section1" }, description = "My Awesome test 100", executionType = ExecutionType.SMOKE)
    public void one()
    {
    }

    @Test(groups = "sample-tests")
    @TestRail(section = { "demo", "sample-section1" }, description = "My Awesome test 101", executionType = ExecutionType.SMOKE)
    public void two()
    {
        Utility.waitToLoopTime(3, "This should be visible");
    }

    @Test(groups = "sample-tests")
    @TestRail(section = { "demo", "sample-section1" }, description = "My Awesome test 102", executionType = ExecutionType.SMOKE)
    public void three()
    {
    }

    @Test(groups = "sample-tests")
    @TestRail(section = { "demo", "sample-section1" }, description = "My Awesome test 103", executionType = ExecutionType.SMOKE)
    public void thisAutomatedTestWillBePublishedInTestRail103()
    {
    }

    @Test(groups = "sample-tests")
    @TestRail(section = { "demo", "sample-section1" }, description = "My Awesome test 104", executionType = ExecutionType.SMOKE)
    public void thisAutomatedTestWillBePublishedInTestRail104()
    {
    }

    @Bug(id = "TTT-2", description = "Bug Description will not be visible due to error thrown")
    @Test(groups = "sample-tests")
    @TestRail(section = { "demo", "sample-section1" }, description = "My Awesome test 105", executionType = ExecutionType.SMOKE)
    public void thisAutomatedTestWillBePublishedInTestRail105()
    {
        Assert.fail();
    }

    @Bug(id = "TTT-1", description = "Bug Description")
    @Test(groups = "sample-tests")
    @TestRail(section = { "demo", "sample-section1" }, description = "My Awesome test 106", executionType = ExecutionType.SMOKE)
    public void thisAutomatedTestWillBePublishedInTestRail106()
    {
    }

}
