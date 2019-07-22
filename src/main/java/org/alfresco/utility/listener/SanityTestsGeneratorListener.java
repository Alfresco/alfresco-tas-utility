
package org.alfresco.utility.listener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.alfresco.utility.LogFactory;
import org.slf4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestNGMethod;
import org.testng.internal.TestNGMethod;

/**
 * Listener class to generate a XML tests suite file, based on given TestGroups added as parameters. To run this
 * listener, see src/main/resources/tas-suite-generator.xml file. By default, the testGroup is set to SANITY, but it can
 * be changed setting testGroup parameter. The package name should point to the package we want to scan for tests. To
 * generate the sanity xml suite: run tas-suite-generator.xml as TestNG suite. Result: the xml sanity suite will be
 * generated on src/main/resources/tas-tests-suite.xml
 * 
 * @author Cristina Diaconu
 */
public class SanityTestsGeneratorListener implements ISuiteListener
{
    private static Logger LOG = LogFactory.getLogger();

    @Override
    public void onStart(ISuite suite)
    {

        // the list of Test Groups to search for
        List<String> projects = resolveTestGroups(suite);
        String xmlFileName = resolveXMLFileName(suite);

        Collection<ITestNGMethod> testsOnRuntime = suite.getAllMethods();
        LOG.info("Total number of tests: " + testsOnRuntime.size());

        Map<String, Collection<ITestNGMethod>> groupsOfTests = suite.getMethodsByGroups();

        Map<String, List<String>> testClasses = new HashMap<String, List<String>>();
        Iterator<ITestNGMethod> iterator;

        for (String key : projects)
        {
            if (groupsOfTests.keySet().contains(key))
            {
                LOG.info("Total number of tests for TestGroup = '" + key + "' is " + groupsOfTests.get(key).size());
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
        writer.generateXmlFile(fullPath(xmlFileName), testClasses, projects.get(0));

        System.exit(0);
    }

    /**
     * Get the TestGroup parameter value from the XML file that run this listener. 
     * The default value for it is Sanity Test Group.
     * 
     * @param suite The tests suite.
     * @return The test group value.
     */
    private List<String> resolveTestGroups(ISuite suite)
    {
        List<String> groups = new ArrayList<String>();
        String testGroup = suite.getParameter("testGroup");
        
        if (testGroup == null) {
        	groups.add("sanity"); // default test group
        	return groups;
        }
        String[] parameters = testGroup.split(",");
        for (String group : parameters) 
        {
        	groups.add(group.trim());
        }

        return groups;
    }
    
    private String resolveXMLFileName(ISuite suite) {
    	
    	String testGroup = suite.getParameter("xmlFileName");
    	if (testGroup == null) {
    		return "tas-sanity-suite.xml"; // default XML file name
    	}
    	return testGroup;
    }
    
    private String fullPath(String xmlFileName) {
    	return "src/main/resources/" + xmlFileName;
    }

    @Override
    public void onFinish(ISuite suite)
    {
        // nothing to do here
    }

}
