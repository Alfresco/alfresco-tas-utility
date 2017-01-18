package org.alfresco.utility.testrail;

import org.alfresco.utility.report.log.Step;
import org.alfresco.utility.testrail.annotation.TestRail;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@ContextConfiguration("classpath:alfresco-tester-context.xml")
@Listeners(value = TestRailExecutorListener.class)
public class RunTestRailIntegrationTest extends AbstractTestNGSpringContextTests
{
    @BeforeClass
    public void aa()
    {
        Step.STEP("data1");
        Step.STEP("data2");
    }

    @Test(groups = "sample-tests")
    @TestRail(section = { "demo", "a", "b" }, description = "My Awesome test 100", executionType = ExecutionType.SMOKE)
    public void ThisIsSomethingNew2()
    {
        Step.STEP("step1");
        Step.STEP("step2");
    }

    @Test(groups = "sample-tests")
    @TestRail(section = { "demo", "a", "b" }, description = "My Awesome test 100", executionType = ExecutionType.SMOKE)
    public void one()
    {
        Step.STEP("step1-one");
        Step.STEP("step2-one");
        Assert.fail();
    }

    @Test(groups = "sample-tests")
    @TestRail(section = { "demo", "a", "b", "c", "d" }, description = "My Awesome test 1", executionType = ExecutionType.SMOKE)
    public void thisAutomatedTestWillBePublishedInTestRail1()
    {
    }

    @Test(groups = "sample-tests")
    @TestRail(section = { "demo", "a", "b", "c", "d" }, description = "My Awesome test 2", executionType = ExecutionType.SMOKE)
    public void thisAutomatedTestWillBePublishedInTestRail2()
    {
    }

    @Test(groups = "sample-tests")
    @TestRail(section = { "demo", "a", "b", "c", "d" }, description = "My Awesome test 3", executionType = ExecutionType.SMOKE)
    public void thisAutomatedTestWillBePublishedInTestRail3()
    {
    }

    @Test(groups = "sample-tests")
    @TestRail(section = { "demo", "a", "b", "c", "d" }, description = "My Awesome test 4", executionType = ExecutionType.SMOKE)
    public void thisAutomatedTestWillBePublishedInTestRail4()
    {
    }

    @Test(groups = "sample-tests")
    @TestRail(section = { "demo", "a", "b", "c", "d" }, description = "My Awesome test 5", executionType = ExecutionType.SMOKE)
    public void thisAutomatedTestWillBePublishedInTestRail5()
    {
    }

    @Test(groups = "sample-tests")
    @TestRail(section = { "demo", "a", "b", "c", "d" }, description = "My Awesome test 6", executionType = ExecutionType.SMOKE)
    public void thisAutomatedTestWillBePublishedInTestRail6()
    {
    }

    @Test(groups = "sample-tests")
    @TestRail(section = { "demo", "a", "b", "c", "d" }, description = "My Awesome test 7", executionType = ExecutionType.SMOKE)
    public void thisAutomatedTestWillBePublishedInTestRail7()
    {
        Assert.fail();
    }

    @Test(groups = "sample-tests")
    @TestRail(section = { "demo", "a", "b", "c", "d" }, description = "My Awesome test 8", executionType = ExecutionType.SMOKE)
    public void thisAutomatedTestWillBePublishedInTestRail8()
    {
    }

    @Test(groups = "sample-tests")
    @TestRail(section = { "demo", "a", "b", "c", "d" }, description = "My Awesome test 9", executionType = ExecutionType.SMOKE)
    public void thisAutomatedTestWillBePublishedInTestRail9()
    {
    }

    @Test(groups = "sample-tests")
    @TestRail(section = { "demo", "a", "b", "c", "d" }, description = "My Awesome test 10", executionType = ExecutionType.SMOKE)
    public void thisAutomatedTestWillBePublishedInTestRail10()
    {
    }

}
