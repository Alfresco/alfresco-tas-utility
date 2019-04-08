
package org.alfresco.utility.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.alfresco.utility.LogFactory;
import org.alfresco.utility.model.TestGroup;
import org.slf4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestNGMethod;
import org.testng.internal.TestNGMethod;

/**
 * Listener class to generate a XML tests suite file, based on given TestGroups added as parameters.
 * 
 * @author Cristina Diaconu
 */
public class SanityTestsGeneratorListener implements ISuiteListener
{
    private static Logger LOG = LogFactory.getLogger();
    private String filePath = "src/main/resources";

    @Override
    public void onStart(ISuite suite)
    {
        // the list of Test Groups to search for
        List<String> projects = new ArrayList<String>(Arrays.asList(TestGroup.SANITY));

        Collection<ITestNGMethod> testsOnRuntime = suite.getAllMethods();
        LOG.info("Total number of tests: " + testsOnRuntime.size());

        Map<String, Collection<ITestNGMethod>> groupsOfTests = suite.getMethodsByGroups();

        Map<String, List<String>> testClasses = new HashMap<String, List<String>>();
        Iterator<ITestNGMethod> iterator;

        for (String key : projects)
        {
            if (groupsOfTests.keySet().contains(key))
            {
                LOG.info("Total number of tests for TestGroup='" + key + "' is " + groupsOfTests.get(key).size());
                iterator = groupsOfTests.get(key).iterator();

                while (iterator.hasNext())
                {
                    TestNGMethod test = (TestNGMethod) iterator.next();

                    // add the class name and the method name to the map
                    String className = test.getMethod().getDeclaringClass().getName();
                    if (testClasses.containsKey(className))
                    {
                        testClasses.get(className).add(test.getMethodName());
                    }
                    else
                    {
                        List<String> methods = new ArrayList<String>();
                        methods.add(test.getMethodName());
                        testClasses.put(className, methods);
                    }
                }
            }
        }

        XmlTestsSuiteWriter writer = new XmlTestsSuiteWriter();
        writer.generateXmlFile(filePath, testClasses);

        System.exit(0);
    }

    @Override
    public void onFinish(ISuite suite)
    {
        // nothing to do here
    }

}
