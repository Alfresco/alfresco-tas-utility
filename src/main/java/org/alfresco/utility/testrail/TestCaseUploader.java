package org.alfresco.utility.testrail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.alfresco.utility.LogFactory;
import org.alfresco.utility.testrail.annotation.SectionUtil;
import org.alfresco.utility.testrail.annotation.TestRail;
import org.alfresco.utility.testrail.model.Run;
import org.alfresco.utility.testrail.model.Section;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.testng.ITestResult;

/**
 * Handle uploads of test cases in test rails based on the sections defined in {@link TestRail} annotation
 * Take a look at {@link TestRailSampleTest} for simple examples
 * Notice that those sections should exist in Test Rail.
 */
public class TestCaseUploader
{
    Logger LOG = LogFactory.getLogger();
    TestRailApi testRail = new TestRailApi();
    private static Map<String, String> testCasesNotUploaded = new HashMap<String, String>();

    private TestRail annotation = null;

    List<Section> allSections = new ArrayList<Section>();
    private Run currentTestRun = null;

    public void oneTimeUpdateFromTestRail()
    {
        if (testRail.hasConfigurationErrors())
            return;

        allSections = testRail.getSectionsOfCurrentProject();
        currentTestRun = testRail.getRunOfCurrentProject();
    }

    public void addTestRailIfNotExist(ITestResult result)
    {
        if (testRail.hasConfigurationErrors())
            return;
        annotation = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(TestRail.class);

        if (annotation != null)
        {
            SectionUtil sectionUtil = new SectionUtil(annotation.section());
            for (Section tmpSection : allSections)
            {
                if (tmpSection.getDepth() == 0 && tmpSection.getName().equals(sectionUtil.getRootSectionName()))
                {
                    sectionUtil.setRootSection(tmpSection);
                    break;
                }
            }
            if (sectionUtil.hasRoot())
            {
                Section lastChildSection = null;
                if(!sectionUtil.getRootChildSections().isEmpty() && annotation.section().length > 0)
                {
                    for (Section tmpSection : allSections)
                    {
                        for (String sectionChild : sectionUtil.getRootChildSections())
                        {
                            if (tmpSection.getName().equals(sectionChild) && tmpSection.getParent_id() == sectionUtil.getRootSection().getId()
                                    && tmpSection.getDepth() > 0)
                            {
                                lastChildSection = tmpSection;
                            }
                        }
                    }
                }
                else
                {
                    lastChildSection = sectionUtil.getRootSection();
                }
                
                if (lastChildSection != null)
                {
                    if (testRail.isAutomatedTestCaseInSection(result.getName(), lastChildSection, annotation))
                    {
                        LOG.info("Test Case [{}] is already uploaded under Section(s) {}.", result.getName(), ArrayUtils.toString(annotation.section()));
                    }
                    else
                    {
                        testRail.addTestCase(result, lastChildSection, annotation);
                        LOG.info("Test Case [{}] marked as [{}] Test Type is uploaded under Section(s) {}.", testRail.getFullTestCaseName(result),
                                annotation.testType().toString(), ArrayUtils.toString(annotation.section()));
                    }
                }
                else
                {
                    testCasesNotUploaded.put(testRail.getFullTestCaseName(result), "Cannot find Section:" + sectionUtil.getRootChildSections().toString()
                            + " having as root, section: " + sectionUtil.getRootSectionName());
                }
            }
            else
            {
                testCasesNotUploaded.put(testRail.getFullTestCaseName(result),
                        "Cannot find root Section in Test Rail named : " + sectionUtil.getRootSectionName());
            }

        }
        else
        {
            testCasesNotUploaded.put(testRail.getFullTestCaseName(result), "Test Case is NOT marked for Test Rail. Use @TestRail annotation.");
        }
    }

    public void updateTestRailTestCase(ITestResult result)
    {
        if (testRail.hasConfigurationErrors())
            return;
        testRail.updateTestCaseResult(result, currentTestRun);
    }

    public void showTestCasesNotUploaded()
    {
        Iterator<Entry<String, String>> entries = testCasesNotUploaded.entrySet().iterator();
        while (entries.hasNext())
        {
            @SuppressWarnings("rawtypes")
            Entry thisEntry = (Entry) entries.next();
            Object key = thisEntry.getKey();
            Object value = thisEntry.getValue();
            LOG.error("Review Test Case [{}], I cannot upload to Test Rail due to: [{}]", key, value);
        }
    }

}
