package org.alfresco.utility.report.log;

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
    
    public static void add(String stepValue)
    {
        XmlLogWritter.testSteps.add(stepValue);
    }
}
