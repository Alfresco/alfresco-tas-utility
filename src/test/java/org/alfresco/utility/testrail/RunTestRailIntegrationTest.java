package org.alfresco.utility.testrail;

import org.alfresco.utility.report.log.Step;
import org.alfresco.utility.testrail.annotation.TestRail;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
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
    @TestRail(section = { "demo", "a", "b", "c" }, description = "My Awesome test 100", executionType = ExecutionType.SMOKE)
    public void ThisIsSomethingNew1()
    {
        Step.STEP("step1");
        Step.STEP("step2");
    }
    
    @Test(groups = "sample-tests")
    @TestRail(section = { "demo", "a", "b1", "c" }, description = "My Awesome test 100", executionType = ExecutionType.SMOKE)
    public void ThisIsSomethingNew2()
    {
        Step.STEP("step11");
        Step.STEP("step22");
    }
    
    @Test(groups = "sample-tests")
    @TestRail(section = { "demo", "a", "b1", "c" }, description = "My Awesome test 100", executionType = ExecutionType.SMOKE)
    public void ThisIsSomethingNew3()
    {
        Step.STEP("step11");
        Step.STEP("step22");
    }

}
