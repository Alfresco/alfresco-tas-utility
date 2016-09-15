package org.alfresco.sample;

import org.alfresco.utility.report.ReportListenerAdapter;
import org.alfresco.utility.testrail.ExecutionType;
import org.alfresco.utility.testrail.annotation.TestRail;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@ContextConfiguration("classpath:alfresco-test-context.xml")
@Listeners(value=ReportListenerAdapter.class)
public class TestRailIntegrationTest extends AbstractTestNGSpringContextTests
{
    @Test(groups="sample-tests")
    @TestRail(section = { "demo", "sample-section" }, description = "My Awesome test", executionType = ExecutionType.SMOKE)
    public void thisAutomatedTestWillBePublishedInTestRail()
    {
        //nothing to do here for now - the test will pass
    }
}
