package org.alfresco.utility.testrail;

import org.alfresco.utility.Utility;
import org.alfresco.utility.testrail.model.Run;
import org.alfresco.utility.testrail.model.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;

public class TestRailStatusUpdaterItem
{
    static Logger LOG = LoggerFactory.getLogger("testrail");
    private ITestResult testResult;
    private Run run;
    private TestCase testCase;

    public ITestResult getTestResult()
    {
        return testResult;
    }

    public void setTestResult(ITestResult testResult)
    {
        this.testResult = testResult;
    }

    public Run getRun()
    {
        return run;
    }

    public void setRun(Run run)
    {
        this.run = run;
    }

    public TestCase getTestCase()
    {
        return testCase;
    }

    public void setTestCase(TestCase testCase)
    {
        this.testCase = testCase;
    }

    public void updateTestCaseResult(TestRailApi testRail)
    {
        testRail.setCurrentTestCase(testCase);
        Object response = testRail.updateTestCaseResult(getTestResult(), getRun());
        if (response != null)
        {
            LOG.info("{} BACKGROUND TASK update test: [{}] with result {}", this.getClass().getSimpleName(), testRail.getFullTestCaseName(getTestResult()),
                    response.toString());
            if (response.toString().contains(TestCaseUploader.MULTIPLE_TESTRAIL_REQUESTS))
            {
                Utility.waitToLoopTime(3);
                testRail.updateTestCaseResult(getTestResult(), getRun());
            }
        }

    }
}
