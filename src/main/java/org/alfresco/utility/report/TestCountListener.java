package org.alfresco.utility.report;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.alfresco.utility.model.TestGroup;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestNGMethod;
import org.testng.internal.TestNGMethod;

public class TestCountListener implements ISuiteListener
{

    FileWriter fileWriter;
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    @Override
    public void onStart(ISuite suite)
    {
        ArrayList<String> projects = new ArrayList<String>(Arrays.asList(TestGroup.REST_API, TestGroup.CMIS, TestGroup.FTP, TestGroup.CIFS, TestGroup.WEBDAV,
                TestGroup.AOS, TestGroup.SMTP, TestGroup.IMAP, TestGroup.INTEGRATION, TestGroup.PREUPGRADE, TestGroup.POSTUPGRADE, TestGroup.EXTENTION_POINTS));

        Collection<ITestNGMethod> testsOnRuntime = suite.getAllMethods();
        System.out.println("TOTAL NUMBER OF TESTS: " + testsOnRuntime.size());

        int bugAnnotation = 0;
        Iterator<ITestNGMethod> iterator = testsOnRuntime.iterator();
        while (iterator.hasNext())
        {
            TestNGMethod test = (TestNGMethod) iterator.next();
            if (test.getMethod().getDeclaredAnnotationsByType(Bug.class).length == 1)
                bugAnnotation++;
        }

        System.out.println("@Bug TESTS: " + bugAnnotation);
        System.out.println("----------");

        try
        {
            createCVSFile();
        }
        catch (IOException e)
        {
            System.out.println("Error while creating fileWriter !!!");
            e.printStackTrace();
        }

        Map<String, Collection<ITestNGMethod>> groupsOfTests = suite.getMethodsByGroups();

        for (String key: projects)
        {

            if(groupsOfTests.keySet().contains(key))
            {
                int sanity = 0;

                int full = 0;
                int core = 0;
                int notAllowed = 0;

                bugAnnotation = 0;

                int bugSanity = 0;
                int bugFull = 0;
                int bugCore = 0;

                System.out.println("----------" + key + "----------");
                System.out.println("TOTAL NUMBER OF TESTS for :  " + key + " - " + groupsOfTests.get(key).size());

                iterator = groupsOfTests.get(key).iterator();
                while (iterator.hasNext())
                {
                    TestNGMethod test = (TestNGMethod) iterator.next();
                    int bugLength = test.getMethod().getDeclaredAnnotationsByType(Bug.class).length;

                    List<String> testgroups = Arrays.asList(test.getGroups());

                    if (testgroups.contains(TestGroup.SANITY))
                    {
                        sanity++;
                        if (bugLength == 1)
                            bugSanity++;
                    }
                    else if (testgroups.contains(TestGroup.CORE))
                    {
                        core++;
                        if (bugLength == 1)
                            bugCore++;
                    }
                    else if (testgroups.contains(TestGroup.FULL))
                    {
                        full++;
                        if (bugLength == 1)
                            bugFull++;
                    }
                    else
                        notAllowed++;

                    if (bugLength == 1)
                        bugAnnotation++;
                }
                try
                {
                    fileWriter.append(key);
                    fileWriter.append(COMMA_DELIMITER);

                    fileWriter.append(String.valueOf(groupsOfTests.get(key).size()));
                    fileWriter.append(COMMA_DELIMITER);

                    fileWriter.append(String.valueOf(bugAnnotation));
                    fileWriter.append(COMMA_DELIMITER);

                    fileWriter.append(String.valueOf(sanity));
                    fileWriter.append(COMMA_DELIMITER);

                    fileWriter.append(String.valueOf(bugSanity));
                    fileWriter.append(COMMA_DELIMITER);

                    fileWriter.append(String.valueOf(core));
                    fileWriter.append(COMMA_DELIMITER);

                    fileWriter.append(String.valueOf(bugCore));
                    fileWriter.append(COMMA_DELIMITER);

                    fileWriter.append(String.valueOf(full));
                    fileWriter.append(COMMA_DELIMITER);

                    fileWriter.append(String.valueOf(bugFull));
                    fileWriter.append(NEW_LINE_SEPARATOR);

                }
                catch (IOException e)
                {
                    System.out.println("Error while writing on fileWriter !!!");
                    e.printStackTrace();
                }

                System.out.println("NUMBER OF TESTS @Bug: " + bugAnnotation);

                System.out.println("NUMBER OF TESTS for :  " + key + " - SANITY " + sanity);
                System.out.println("NUMBER OF TESTS Bug - SANITY " + bugSanity);
                System.out.println("NUMBER OF TESTS for:  " + key + " - CORE " + core);
                System.out.println("NUMBER OF TESTS Bug - CORE " + bugCore);
                System.out.println("NUMBER OF TESTS for:  " + key + " - FULL " + full);
                System.out.println("NUMBER OF TESTS Bug - FULL " + bugFull);
                System.out.println("NUMBER OF TESTS for:  " + key + " - NO PHASE " + notAllowed);

                System.out.println("----------");
            }
        }

        try
        {

            fileWriter.flush();
            fileWriter.close();
        }
        catch (IOException e)
        {
            System.out.println("Error while flushing/closing fileWriter !!!");
            e.printStackTrace();
        }
        System.exit(0);
    }

    @Override
    public void onFinish(ISuite suite)
    {
    }

    private void createCVSFile() throws IOException
    {

        String FILE_HEADER = "TASRegression,total,totalbugs,sanity,sanitybugs,core,curebugs,full,fullbugs";

        fileWriter = new FileWriter("FullRegression.cvs");
        fileWriter.append(FILE_HEADER.toString());
        fileWriter.append(NEW_LINE_SEPARATOR);
    }

}
