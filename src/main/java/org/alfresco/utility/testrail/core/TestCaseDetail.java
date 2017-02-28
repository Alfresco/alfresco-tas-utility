package org.alfresco.utility.testrail.core;

import java.util.List;

import org.alfresco.utility.report.Bug;
import org.alfresco.utility.report.log.Step;
import org.alfresco.utility.testrail.annotation.TestRail;
import org.alfresco.utility.testrail.model.Section;
import org.alfresco.utility.testrail.model.TestCase;
import org.testng.ITestResult;

/**
 * Saves the information of a test case that is executed
 */
public class TestCaseDetail
{
    private String id;
    private String name;
    private TestRail annotation = null;
    private Bug bugAnnotated = null;
    private TestCaseDestination testCaseDestination = new TestCaseDestination();
    private TestCase testRailObject = null;
    private ITestResult result;
    private long elapsed;

    /* this are the actual test steps */
    private String notes = "";

    public String getNotes()
    {
        return notes;
    }

    public TestCaseDetail(ITestResult currentTest)
    {
        setResult(currentTest);
        id = String.format("%s#%s", currentTest.getInstanceName(), currentTest.getMethod().getMethodName());
        Object[] objs = currentTest.getParameters();
        if(objs.length > 0)
        {
            // test has @DataProvider
            name = objs[0].toString();
        }
        else
        {
            name = currentTest.getMethod().getMethodName();
        }

        annotation = currentTest.getMethod().getConstructorOrMethod().getMethod().getAnnotation(TestRail.class);
        bugAnnotated = currentTest.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Bug.class);
        
        testCaseDestination.fromAnnotation(annotation);
        setElapsed(currentTest.getEndMillis() - currentTest.getStartMillis());

        StringBuilder notesSB = new StringBuilder("");
        if (Step.testSteps.get(getResult().getTestClass().getName()) != null)
        {
            notesSB.append("Dataprep\n========\n* ").append(String.join("\n* ", Step.testSteps.get(result.getTestClass().getName())));
        }

        if (Step.testSteps.get(result.getMethod().getMethodName()) != null)
        {
            notesSB.append("\n\nTestCase Steps\n=========\n* ").append(String.join("\n* ", Step.testSteps.get(result.getMethod().getMethodName())));
        }
        notes = notesSB.toString();
    }

    public Bug getBugDetails()
    {
        return bugAnnotated;
    }

    public TestCaseDestination getTestCaseDestination()
    {
        return testCaseDestination;
    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public TestRail getTestRailAnnotation()
    {
        return annotation;
    }

    public boolean isMarkForUpload()
    {
        return getTestRailAnnotation() != null;
    }

    public boolean hasSectionCreatedIn(List<Section> allSections)
    {
        /*
         * root section has depth='0'
         * First get the root section. We need to know the ID of the root section
         * Based on this id get the child section and continue until the last child
         * Save the last child section because this will be the destination section
         */
        for (Section tmpSection : allSections)
        {
            if (tmpSection.getDepth() == 0 && tmpSection.getName().equals(getTestCaseDestination().getRootSectionName()))
            {
                getTestCaseDestination().setRootSection(tmpSection);
                break;
            }
        }

        if (getTestCaseDestination().hasRootSection())
        {
            Section parent = getTestCaseDestination().getRootSection();
            int depth = 0;
            for (String destinationSection : getTestCaseDestination().getChildDestinationSection())
            {
                depth += 1;
                for (Section s : allSections)
                {
                    if (s.getName().equals(destinationSection) && s.getDepth() == depth && s.getParent_id() == parent.getId())
                    {
                        parent = s;
                        if (depth == getTestCaseDestination().getChildDestinationSection().size())
                        {
                            getTestCaseDestination().setDestination(s);
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        else
        {
            // if we didn't find the root then we can consider the destination section is not created.
            return false;
        }
    }

    public TestCase getTestRailObject()
    {
        return testRailObject;
    }

    public void setTestRailObject(TestCase testRailObject)
    {
        this.testRailObject = testRailObject;
    }

    public int getStatusId()
    {
        int status = 2; // blocked in Test Rail

        switch (getResult().getStatus())
        {
            case ITestResult.SUCCESS:
                status = 1; // Passed in Test Rail
                break;
            case ITestResult.FAILURE:
                status = 5; // Failed in Test Rail
                break;
            case ITestResult.SKIP:
                status = 4; // Retest in TestRail
                break;
            case ITestResult.SUCCESS_PERCENTAGE_FAILURE:
                status = 1;
                break;
            default:
                break;
        }
        return status;
    }

    public ITestResult getResult()
    {
        return result;
    }

    public void setResult(ITestResult result)
    {
        this.result = result;
    }

    public long getElapsed()
    {
        return elapsed;
    }

    public void setElapsed(long elapsed)
    {

        this.elapsed = elapsed;
    }

    public String getElapsedString()
    {
        if (getElapsed() <= 1000)
            return "1s";
        else
        {
            return String.valueOf(elapsed / 1000) + "s";
        }
    }
}
