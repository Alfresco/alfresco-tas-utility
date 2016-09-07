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
    
    private static void addStep(String stepValue)
    {
        XmlLogWritter.testSteps.add(stepValue);
    }
    
    public static void STEP(String name){
        addStep(name);
    }
}
