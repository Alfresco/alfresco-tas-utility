package org.alfresco.utility.testrail.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.alfresco.utility.testrail.annotation.TestRail;
import org.alfresco.utility.testrail.model.Section;

/**
 * Saves the details of all Section assigned for a Test Case
 * the string passed from annotation, the root Section object from TestRail and the destination child Section
 */
public class TestCaseDestination
{
    /*
     * Define the Test Rail sections where the current test will be created
     * section = {"level1", "level2", "level3"}
     */
    private List<String> sections = new ArrayList<String>();
    private Section rootSection = null;
    private Section destination = null;

    protected void fromAnnotation(TestRail testRailAnnotation)
    {
        if (testRailAnnotation != null)
        {
            for (int i = 0; i < testRailAnnotation.section().length; i++)
            {
                if (testRailAnnotation.section()[i] != null)
                {
                    if (testRailAnnotation.section()[i].contains("|"))
                    {
                        sections.addAll(Arrays.asList(testRailAnnotation.section()[i].split(Pattern.quote("|"), -1)));
                    }
                    else
                    {
                        sections.add(testRailAnnotation.section()[i]);
                    }
                }
            }
        }
    }

    public String getRootSectionName()
    {
        return getDestinationSections().get(0);
    }

    public List<String> getDestinationSections()
    {
        return sections;
    }

    public Section getRootSection()
    {
        return rootSection;
    }

    public void setRootSection(Section rootSection)
    {
        this.rootSection = rootSection;
    }

    public boolean hasRootSection()
    {
        return this.rootSection != null;
    }

    public boolean hasDestination()
    {
        return this.destination != null;
    }

    public Section getDestination()
    {
        return destination;
    }

    public void setDestination(Section destination)
    {
        this.destination = destination;
    }

    /**
     * @return all Sections of current Test Case without root Section
     */
    public List<String> getChildDestinationSection()
    {
        List<String> remaining = new ArrayList<String>();
        if (getDestinationSections().size() > 0)
            for (int i = 1; i < getDestinationSections().size(); i++)
            {
                remaining.add(getDestinationSections().get(i));
            }
        return remaining;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder("[sections()=|");
        for (String s : getDestinationSections())
        {
            sb.append(s).append("|");
        }
        sb.append("]");
        return sb.toString();
    }

}