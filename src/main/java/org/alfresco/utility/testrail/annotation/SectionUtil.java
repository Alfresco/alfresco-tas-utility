package org.alfresco.utility.testrail.annotation;

import java.util.ArrayList;
import java.util.List;

import org.alfresco.utility.testrail.model.Section;

public class SectionUtil
{
    String[] section;

    private Section rootSection = null;
    private Section lastSection = null;

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

    public SectionUtil(String[] section, List<Section> allSections)
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

    public void setLastSection(Section lastSection)
    {
        this.lastSection = lastSection;
    }

    public boolean hasRoot()
    {
        return getRootSection() != null;
    }

}
