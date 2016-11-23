package org.alfresco.utility.report.log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.alfresco.utility.LogFactory;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class Step
{
    private String value;
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

        for (StackTraceElement stack : stackTrace)
        {

            Class<?> newClass = null;
            String methodNameKey = null;
            try
            {
                newClass = Class.forName(stack.getClassName());
                if (newClass.getAnnotation(Test.class) != null)
                {
                    Method method = newClass.getDeclaredMethod(stack.getMethodName());
                    if (method.getAnnotation(BeforeClass.class) != null)
                    {
                        methodNameKey = stack.getClassName();
                    }
                    else
                    {
                        methodNameKey = stack.getMethodName();
                    }

                    if (!testSteps.containsKey(methodNameKey))
                    {
                        testSteps.put(methodNameKey, new ArrayList<String>());
                    }
                    testSteps.get(methodNameKey).add(stepValue);
                    break;
                }

            }
            catch (Exception e)
            {
                LogFactory.getLogger().error(e.getMessage());
            }

        }

    }
}
