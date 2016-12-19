package org.alfresco.utility.report;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestNGMethod;
import org.testng.internal.TestNGMethod;

public class TestCountListener implements ISuiteListener
{

    @Override
    public void onStart(ISuite suite)
    {
        Collection<ITestNGMethod> testsOnRuntime = suite.getAllMethods();
        System.out.println("TOTAL NUMBER OF TESTS: " + testsOnRuntime.size());

        Map<String, Collection<ITestNGMethod>> groupsOfTests = suite.getMethodsByGroups();

        for (String key : groupsOfTests.keySet())
        {
            System.out.println("TOTAL NUMBER OF TESTS PER GROUP:  " + key + " - " + groupsOfTests.get(key).size());
        }

        int bugAnnotation = 0;
        Iterator<ITestNGMethod> iterator = testsOnRuntime.iterator();
        while (iterator.hasNext())
        {
            TestNGMethod test = (TestNGMethod) iterator.next();
            if (test.getMethod().getDeclaredAnnotationsByType(Bug.class).length == 1)
                bugAnnotation++;
        }

        System.out.println("@Bug TESTS: " + bugAnnotation);
        System.exit(0);
    }

    @Override
    public void onFinish(ISuite suite)
    {
    }

}
