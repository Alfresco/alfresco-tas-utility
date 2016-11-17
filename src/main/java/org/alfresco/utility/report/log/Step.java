package org.alfresco.utility.report.log;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.alfresco.utility.LogFactory;

public class Step
{
    private String value;
    
    private static String testAnnotation = "@org.testng.annotations.Test";

    public static Map<String, ArrayList<String>> testSteps = Collections.synchronizedMap(new HashMap<String, ArrayList<String>>());

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
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        for (StackTraceElement elem : stackTrace)
        {

            Class<?> newClass = null;
            String methodNameKey = null;
            try
            {
                newClass = Class.forName(elem.getClassName());
                for (Annotation annotation : newClass.getDeclaredAnnotations())
                {
                    if (annotation.toString().contains(testAnnotation))
                    {
                        methodNameKey = elem.getMethodName();
                        if (!testSteps.containsKey(methodNameKey))
                        {
                            testSteps.put(methodNameKey, new ArrayList<String>());
                        }
                        testSteps.get(methodNameKey).add(stepValue);
                        return;
                    }
                }

            }
            catch (ClassNotFoundException e)
            {

            }
        }

    }
}
