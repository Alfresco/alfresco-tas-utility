package org.alfresco.utility.testrail.annotation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.alfresco.utility.testrail.model.Section;

public class SectionUtil
{
    /*
     * retain all the section annotated in tests
     */
    public String[] section;
    private Section rootSection = null;
    private String sectionSplit = "|";

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
        List<String> sections = new ArrayList<String>();
        for (int i = 0; i < section.length; i++)
        {
            if (section[i] != null)
            {
                if (section[i].contains("|"))
                {
                    sections.addAll(Arrays.asList(section[i].split(Pattern.quote(sectionSplit),-1)));
                }
                else
                {
                    sections.add((String)section[i]);
                }

            }
        }
        this.section = sections.toArray(new String[sections.size()]);

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
