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
import org.alfresco.utility.testrail.model.Section;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.testng.ITestResult;

public class TestCaseUploader
{
    Logger LOG = LogFactory.getLogger();
    TestRailApi testRail = new TestRailApi();
    private static Map<String, String> testCasesNotUploaded = new HashMap<String, String>();

    private TestRail annotation = null;

    List<Section> allSections = new ArrayList<Section>();

    public TestCaseUploader()
    {
        allSections = testRail.getSections(1);
    }

    public void addTestRailIfNotExist(ITestResult result)
    {
        annotation = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(TestRail.class);

        if (annotation != null)
        {

            SectionUtil sectionUtil = new SectionUtil(annotation.section(), allSections);

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
                if (lastChildSection != null)
                {
                    if (testRail.isAutomatedTestCaseInSection(result.getName(), lastChildSection, annotation))
                    {
                        LOG.info("Test Case [{}] is already uploaded under section(s) {}.", result.getName(), ArrayUtils.toString(annotation.section()));
                    }
                    else
                    {
                        testRail.addTestCase(result.getName(), lastChildSection, annotation);
                        LOG.info("Test Case [{}] marked as [{}] Test Type is uploaded under section(s) {}.", result.getName(), annotation.type().toString(),
                                ArrayUtils.toString(annotation.section()));
                    }
                }
                else
                {
                    testCasesNotUploaded.put(result.getName(), "Cannot find section:" + sectionUtil.getRootChildSections().toString()
                            + " having as root, section: " + sectionUtil.getRootSectionName());
                }
            }
            else
            {
                testCasesNotUploaded.put(result.getName(), "Cannot find root Section in Test Rail: " + sectionUtil.getRootSectionName());
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

    public void showTestCasesNotUploaded()
    {
        Iterator<Entry<String, String>> entries = testCasesNotUploaded.entrySet().iterator();
        while (entries.hasNext())
        {
            @SuppressWarnings("rawtypes")
            Entry thisEntry = (Entry) entries.next();
            Object key = thisEntry.getKey();
            Object value = thisEntry.getValue();
            LOG.error("Review Test Case {}, cannot upload to Test Rail due to: {}", key, value);
        }
    }

}
