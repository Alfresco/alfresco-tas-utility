package org.alfresco.utility.testng;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.alfresco.utility.model.TestGroup;
import org.alfresco.utility.report.Bug;
import org.apache.commons.lang.SystemUtils;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.testng.internal.ConstructorOrMethod;

/**
 * Use this listener if you want to exclude automatically tests based on your operating system.
 * Example:
 * <code>
 * &#64;Test(groups = { TestGroup.OS_WIN })
 * public void testA()
 * {
 * }
 * 
 * &#64;Test(groups = { TestGroup.OS_WIN, TestGroup.OS_UNIX })
 * public void testB()
 * {
 * }
 * 
 * &#64;Test(groups = { TestGroup.OS_WIN })
 * public void testC()
 * {
 * }
 * 
 * and you are running your tests on Unix, then only testB will be executed, the remaining ones will be marked as skipped.
 * 
 * </code>
 * 
 * @author Paul Brodner
 */
public class OSTestMethodSelector implements IInvokedMethodListener
{
    @Override
    public void beforeInvocation(IInvokedMethod testNGmethod, ITestResult testResult)
    {
        ConstructorOrMethod contructorOrMethod = testNGmethod.getTestMethod().getConstructorOrMethod();
        Method method = contructorOrMethod.getMethod();
        if (method != null)
        {
            if (method.isAnnotationPresent(Test.class))
            {
                Test testClass = method.getAnnotation(Test.class);

                String runBugs = System.getProperty("runBugs");
                if (runBugs!=null && runBugs.equals("false"))
                {
                    if (method.isAnnotationPresent(Bug.class))
                    {
                        Bug bug = method.getAnnotation(Bug.class);
                        throw new SkipException(
                                String.format("This test was skiped because was marked as BUG: {[id='%s', description='%s']}.(info: you can run tests marked with bugs, passing -DrunBugs=true)", bug.id(), bug.description()));
                    }
                }

                List<String> groups = Arrays.asList(testClass.groups());

                if (groups != null)
                {
                    if (groups.contains(TestGroup.OS_LINUX) || groups.contains(TestGroup.OS_WIN))
                    {
                        if (SystemUtils.IS_OS_LINUX && !groups.contains(TestGroup.OS_LINUX))
                        {
                            throw new SkipException(
                                    String.format("This test was skipped because it was marked to be executed on differed operating system(s). Groups used: %s and was executed on: %s",
                                            String.valueOf(groups), System.getProperty("os.name")));
                        }
                        
                        else if (SystemUtils.IS_OS_MAC && !groups.contains(TestGroup.OS_MAC))
                        {
                            throw new SkipException(
                                    String.format("This test was skipped because it was marked to be executed on differed operating system(s). Groups used: %s and was executed on: %s",
                                            String.valueOf(groups), System.getProperty("os.name")));
                        }
                        
                        else if (SystemUtils.IS_OS_WINDOWS && !groups.contains(TestGroup.OS_WIN))
                        {
                            throw new SkipException(
                                    String.format("This test was skipped because it was marked to be executed on differed operating system(s). Groups used: %s and was executed on: %s",
                                            String.valueOf(groups), System.getProperty("os.name")));
                        }

                        
                        

                    }
                }
            }
        }

    }

    @Override
    public void afterInvocation(IInvokedMethod testNGmethod, ITestResult testResult)
    {       
    }

}
