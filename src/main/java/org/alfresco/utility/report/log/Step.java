package org.alfresco.utility.report.log;

import java.util.ArrayList;
import java.util.List;

import org.alfresco.utility.LogFactory;

public class Step
{
    private String value;

    public static List<String> testSteps = new ArrayList<String>();
    
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
        LogFactory.getLogger().info("STEPS:" + stepValue);
        testSteps.add(stepValue);
    }
}
