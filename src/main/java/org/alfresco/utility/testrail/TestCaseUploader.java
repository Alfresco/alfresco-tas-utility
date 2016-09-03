package org.alfresco.utility.testrail;

import java.util.ArrayList;
import java.util.List;

import org.alfresco.utility.LogFactory;
import org.alfresco.utility.testrail.model.Section;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.testng.ITestResult;

public class TestCaseUploader
{
    Logger LOG = LogFactory.getLogger();
    TestRailApi testRail = new TestRailApi();

    private TestRail annotation = null;

    List<Section> allSections = new ArrayList<Section>();

    public TestCaseUploader()
    {
        allSections = testRail.getSections(1);
    }

    public void addInTestRailIfNotExist(ITestResult result)
    {
        annotation = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(TestRail.class);
        if (annotation != null)
        {
            LOG.info("Test Case [{}] marked as [{}] Test Type is uploaded under section(s) [{}].", result.getName(), annotation.type().toString(),
                    ArrayUtils.toString(annotation.level()));
            /*
             * rest-api
             *   comments
             *      node
             */
            for (Section section : allSections)
            {
                
                if (annotation.level().equals(section.getName()))
                {
                    testRail.addTestCase(result.getName(), section);
                    return;
                }
            }
        }
        else
        {
            LOG.info("Test Case [{}] is NOT marked for Test Rail.", result.getName());
        }
    }

    public void updateTestRailTestCase(ITestResult result)
    {
        testRail.updateTestCaseResult(result);
    }

}
