package org.alfresco.utility.report.log;

import org.alfresco.utility.LogFactory;

public class Step
{
    private String value;

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public static void STEP(String stepValue)
    {
        LogFactory.getLogger().info(stepValue);
        XmlLogWritter.testSteps.add(stepValue);
    }
}
