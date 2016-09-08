package org.alfresco.utility.testrail.annotation;

import java.util.ArrayList;
import java.util.List;

import org.alfresco.utility.testrail.model.Section;

public class SectionUtil
{
    /*
     * retain all the section annotated in tests
     */
    String[] section;
    private Section rootSection = null;

    /*
     * get all the section names without the root name
     */
    public List<String> getRootChildSections()
    {
        List<String> remaining = new ArrayList<String>();
        if (section.length > 0)
            for (int i = 1; i < section.length; i++)
            {
                remaining.add(section[i]);
            }
        return remaining;
    }

    public SectionUtil(String[] section)
    {
        this.section = section;
    }

    public String getRootSectionName()
    {
        return this.section[0];
    }

    public void setRootSection(Section section)
    {
        this.rootSection = section;
    }

    public Section getRootSection()
    {
        return this.rootSection;
    }

    public boolean hasRoot()
    {
        return getRootSection() != null;
    }

}
