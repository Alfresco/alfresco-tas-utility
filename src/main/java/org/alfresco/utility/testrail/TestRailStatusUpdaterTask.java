package org.alfresco.utility.testrail;

import java.util.ArrayList;
import java.util.List;

import org.alfresco.utility.testrail.model.Run;
import org.alfresco.utility.testrail.model.TestCase;
import org.testng.ITestResult;

/**
 * @author Paul Brodner
 */
public class TestRailStatusUpdaterTask implements Runnable
{
    private List<TestRailStatusUpdaterItem> itemToComplete = new ArrayList<TestRailStatusUpdaterItem>();
    private TestRailApi testRail = null;

    public TestRailStatusUpdaterTask(TestRailApi testRail)
    {
        this.testRail = testRail;
    }

    public void addToBackgroundJob(ITestResult result, Run run, TestCase testCase)
    {
        TestRailStatusUpdaterItem tmp = new TestRailStatusUpdaterItem();
        tmp.setRun(run);
        tmp.setTestResult(result);
        tmp.setTestCase(testCase);
        itemToComplete.add(tmp);
    }

    public int remainingTestsToUpdate()
    {
        return this.itemToComplete.size();
    }

    public boolean weHaveItemsInList()
    {
        return remainingTestsToUpdate() > 0;
    }

    @Override
    public void run()
    {       
        while (weHaveItemsInList())
        {           
            TestRailStatusUpdaterItem tmp = itemToComplete.remove(0);
            tmp.updateTestCaseResult(testRail);            
        }
    }

}