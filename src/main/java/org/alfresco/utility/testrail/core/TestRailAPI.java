package org.alfresco.utility.testrail.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.DatatypeConverter;

import org.alfresco.utility.Utility;
import org.alfresco.utility.testrail.ExecutionType;
import org.alfresco.utility.testrail.model.Result;
import org.alfresco.utility.testrail.model.Run;
import org.alfresco.utility.testrail.model.RunTestCase;
import org.alfresco.utility.testrail.model.Section;
import org.alfresco.utility.testrail.model.TestCase;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * TestRail Interaction
 */
public class TestRailAPI
{
    static Logger LOG = LoggerFactory.getLogger("testrail");
    /*
     * Test Rail Template:
     * 1 - Test Case
     */
    private static final int TEMPLATE_ID = new Integer(1);
    private static final int TEST_PRIORITY_MEDIUM = 2;

    Properties properties = new Properties();
    private String username;

    private String password;

    private String endPointApiPath;

    private int currentProjectID;

    private Run currentRun = null;
    private String currentTestRunName;

    private Integer currentSuiteID;

    private String serverUrl;

    private int waitRateLimitBetweenRequests = 0;

    public TestRailAPI()
    {
        readTestRailManagementSection();
    }

    public Run getCurrentRun()
    {
        return currentRun;
    }

    public int getWateRateLimit()
    {
        return waitRateLimitBetweenRequests;
    }

    public void waitForRateLimit()
    {
        Utility.waitToLoopTime(getWateRateLimit(), "Based on Rate Limit defined in current *.properties file.");
    }

    private boolean readTestRailManagementSection()
    {
        boolean configurationError = true;
        InputStream defaultPropsInputStream = getClass().getClassLoader().getResourceAsStream(Utility.getEnvironmentPropertyFile());
        if (defaultPropsInputStream != null)
        {
            try
            {
                properties.load(defaultPropsInputStream);

                this.username = Utility.getSystemOrFileProperty("testManagement.username", properties);
                Utility.checkObjectIsInitialized(username, "username");

                this.password = Utility.getSystemOrFileProperty("testManagement.apiKey", properties);
                Utility.checkObjectIsInitialized(password, "password");

                this.endPointApiPath = Utility.getSystemOrFileProperty("testManagement.endPoint", properties) + "index.php?/api/v2/";
                Utility.checkObjectIsInitialized(endPointApiPath, "endPointApiPath");

                this.currentProjectID = Integer.parseInt(Utility.getSystemOrFileProperty("testManagement.project", properties));
                Utility.checkObjectIsInitialized(currentProjectID, "currentProjectID");

                this.currentTestRunName = Utility.getSystemOrFileProperty("testManagement.testRun", properties);
                Utility.checkObjectIsInitialized(currentTestRunName, "currentTestRunName");

                this.currentSuiteID = Integer.valueOf(Utility.getSystemOrFileProperty("testManagement.suiteId", properties));
                Utility.checkObjectIsInitialized(currentSuiteID, "currentSuiteID");

                if (Utility.getSystemOrFileProperty("testManagement.rateLimitInSeconds", properties) == null)
                    this.waitRateLimitBetweenRequests = 1;
                else
                    this.waitRateLimitBetweenRequests = Integer.valueOf(Utility.getSystemOrFileProperty("testManagement.rateLimitInSeconds", properties));
                /*
                 * alfresco.scheme=http
                 * alfresco.server=localhost
                 * alfresco.port=8080
                 */
                this.serverUrl = String.format("%s://%s:%s/alfresco", Utility.getSystemOrFileProperty("alfresco.scheme", properties),
                        Utility.getSystemOrFileProperty("alfresco.server", properties), Utility.getSystemOrFileProperty("alfresco.port", properties));
                configurationError = false;
            }
            catch (Exception e)
            {
                LOG.error("Cannot initialize Test Management Setting from default.properties file");
            }
        }
        else
        {
            LOG.error("Cannot initialize Test Management Setting from {} file", Utility.getEnvironmentPropertyFile());
        }

        return configurationError;
    }

    public List<Section> getSections(int projectID)
    {
        LOG.info("Query: Test Rail for all Section of currentProject [{}] defined in *.properties file", currentProjectID);
        Object response;
        try
        {
            response = getRequest("get_sections/" + projectID + "&suite_id=" + currentSuiteID);
            return toCollection(response, Section.class);
        }
        catch (Exception e)
        {
            LOG.error(e.getMessage());
        }
        return new ArrayList<Section>();
    }

    public List<TestCase> getAllTestCases(int projectID)
    {
        LOG.info("Query: Test Rail for all TestCases of currentProject [{}] defined in *.properties file", currentProjectID);
        Object response;
        try
        {
            response = getRequest("get_cases/" + projectID + "&suite_id=" + currentSuiteID);
            List<TestCase> tcs = toCollection(response, TestCase.class);
            LOG.info("Found #{} testcases in TestRail.", tcs.size());
            return tcs;
        }
        catch (Exception e)
        {
            LOG.error(e.getMessage());
        }
        return new ArrayList<TestCase>();
    }

    public List<Run> getRuns(int projectID)
    {
        Object response;
        LOG.info("Query: Test Rail for all Runs of currentProject [{}] defined in *.properties file", currentProjectID);
        try
        {
            response = getRequest("get_runs/" + projectID);
            return toCollection(response, Run.class);
        }
        catch (Exception e)
        {
            LOG.error(e.getMessage());
        }
        return new ArrayList<Run>();
    }

    protected Object getRequest(String path) throws Exception
    {
        URL endPointURL = new URL(endPointApiPath + path);
        LOG.info("TestRAILAPI: GetRequest: {}", endPointURL.toURI().toString());
        HttpURLConnection conn = (HttpURLConnection) endPointURL.openConnection();
        conn.addRequestProperty("Content-Type", "application/json");

        conn.addRequestProperty("Authorization", "Basic " + DatatypeConverter.printBase64Binary(String.format("%s:%s", username, password).getBytes()));

        return parseRespose(conn);
    }

    protected Object postRequest(String path, Object data) throws Exception
    {
        LOG.debug("TestRAILAPI: PostRequest: {}", path);
        URL endPointURL = new URL(endPointApiPath + path);
        HttpURLConnection conn = (HttpURLConnection) endPointURL.openConnection();
        conn.addRequestProperty("Content-Type", "application/json");

        conn.addRequestProperty("Authorization", "Basic " + DatatypeConverter.printBase64Binary(String.format("%s:%s", username, password).getBytes()));
        if (data != null)
        {
            byte[] block = JSONValue.toJSONString(data).getBytes("UTF-8");
            conn.setDoOutput(true);
            OutputStream ostream = conn.getOutputStream();
            ostream.write(block);
            ostream.flush();
        }

        return parseRespose(conn);
    }

    /**
     * Parse the response and transform the JSON into object class.
     * 
     * @param conn
     * @return
     * @throws Exception
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    private Object parseRespose(HttpURLConnection conn) throws Exception, IOException, UnsupportedEncodingException
    {
        int status = conn.getResponseCode();
        InputStream istream;
        if (status != 200)
        {
            istream = conn.getErrorStream();
            if (istream == null)
            {
                throw new Exception("TestRail API return HTTP " + status + " (No additional error message received)");
            }
        }
        else
        {
            istream = conn.getInputStream();
        }

        String text = "";
        if (istream != null)
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(istream, "UTF-8"));

            String line;
            while ((line = reader.readLine()) != null)
            {
                text += line;
                text += System.getProperty("line.separator");
            }
            reader.close();
        }

        Object result;
        if (text != "")
        {
            result = JSONValue.parse(text);
        }
        else
        {
            result = new JSONObject();
        }

        if (status != 200)
        {
            String error = "No additional error message received";
            if (result != null && result instanceof JSONObject)
            {
                JSONObject obj = (JSONObject) result;
                if (obj.containsKey("error"))
                {
                    error = '"' + (String) obj.get("error") + '"';
                }
            }

            throw new Exception("TestRail API returned HTTP " + status + "(" + error + ")");
        }
        return result;
    }

    /**
     * Parse the response received and convert it to <Class<T>> passed as parameter
     * 
     * @param response
     * @param classz
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <T> List<T> toCollection(Object response, Class<T> classz)
    {
        ObjectMapper mapper = new ObjectMapper();
        List<Section> list = null;
        try
        {
            list = mapper.readValue(response.toString(), TypeFactory.defaultInstance().constructCollectionType(List.class, classz));
        }
        catch (JsonParseException e)
        {
            LOG.error(e.getMessage());
        }
        catch (JsonMappingException e)
        {
            LOG.error(e.getMessage());
        }
        catch (IOException e)
        {
            LOG.error(e.getMessage());
        }
        return (List<T>) list;
    }

    /**
     * Returns the current Test Runs of current project
     */
    @SuppressWarnings("unchecked")
    public Run getRunOfCurrentProject()
    {
        Run r = getRun(currentTestRunName, currentProjectID);
        if (r == null)
        {
            @SuppressWarnings("rawtypes")
            Map data = new HashMap();
            data.put("suite_id", currentSuiteID);
            data.put("name", currentTestRunName);
            data.put("include_all", !Utility.isPropertyEnabled("testManagement.includeOnlyTestCasesExecuted"));
            data.put("description", "**Server:** " + serverUrl);

            LOG.info("Add new RUN [{}]", currentTestRunName);
            Object response;
            try
            {
                response = postRequest("add_run/" + currentProjectID, data);
                r = toClass(response, Run.class);
            }
            catch (Exception e)
            {
                LOG.error("Cannot add new section: {}", e.getMessage());
            }
        }
        currentRun = r;
        return r;
    }

    /**
     * Perform TestRAIL query of all runs and return the {@link Run} object related to <projectId> and run <name>
     * 
     * @param name
     * @param projectID
     * @return
     */
    public Run getRun(String name, int projectID)
    {
        for (Run run : getRuns(projectID))
        {
            if (run.getName().equals(name) && !run.isIs_completed())
                return run;
        }
        return null;
    }

    protected <T> T toClass(Object response, Class<T> classz)
    {
        ObjectMapper mapper = new ObjectMapper();

        try
        {
            return mapper.readValue(response.toString(), classz);
        }
        catch (JsonParseException e)
        {
            LOG.error(e.getMessage());
        }
        catch (JsonMappingException e)
        {
            LOG.error(e.getMessage());
        }
        catch (IOException e)
        {
            LOG.error(e.getMessage());
        }
        return null;
    }

    /**
     * @return all {@link Section} from TestRail from current project
     */
    public List<Section> getSectionsOfCurrentProject()
    {
        return getSections(currentProjectID);
    }

    public List<TestCase> getAllTestCasesFromCurrentProject()
    {
        return getAllTestCases(currentProjectID);
    }

    /**
     * Update current test run and select/add in test run ONLY the executed test cases
     * 
     * @param currentTestCases
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void updateTestRunWithSelectedTestCases(List<TestCaseDetail> currentTestCases) throws Exception
    {
        Map testCasesData = new HashMap();
        testCasesData.put("include_all", false); //we don't want to include all tests from Test Run, just the ones that we run now
        List cases = new ArrayList();

        /*
         * 1. now save all test cases that exists in current test run
         */
        Object response = getRequest("get_tests/" + currentRun.getId());
        List<RunTestCase> runCases = toCollection(response, RunTestCase.class);
        for (RunTestCase runCase : runCases)
        {
            cases.add(runCase.getCase_id());
        }

        /*
         * 2. save first the results that already exists in current test run
         */
        if (!runCases.isEmpty())
        {
            response = getRequest("get_results_for_run/" + currentRun.getId());
            List<Result> results = toCollection(response, Result.class);
            if (!results.isEmpty())
            {
                for (Result result : results)
                {
                    cases.add(result.getTest_id());
                }
            }
        }

        /*
         * 3. now update the test run with ONLY the test cases that are executed in this run
         */

        for (TestCaseDetail tc : currentTestCases)
        {
            cases.add(tc.getTestRailObject().getId());
        }

        testCasesData.put("case_ids", cases);
        LOG.info("TestRAILAPI: Update Test Run: {} with only selected Test Cases of this run #{} + existing test cases already executed #{} ",
                currentRun.getName(), currentTestCases.size(), runCases.size());
        try
        {
            response = postRequest("update_run/" + currentRun.getId(), testCasesData);
        }
        catch (Exception e1)
        {
            LOG.error("Cannot update RUN {} with curent test cases executed. Error: {}", currentRun.getName(), e1.getMessage());
        }

    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Object addResultsForCases(List<TestCaseDetail> currentTestCases) throws Exception
    {
        Map data = new HashMap();
        List results = new ArrayList();

        for (TestCaseDetail tc : currentTestCases)
        {
            Map result = new HashMap();
            result.put("case_id", tc.getTestRailObject().getId());
            result.put("status_id", tc.getStatusId());
            result.put("comment", "Executed by awesome TAS project");
            result.put("elapsed", tc.getElapsedString());

            if (tc.getBugDetails() != null)
            {
                result.put("defects", tc.getBugDetails().id());
                result.put("comment", tc.getBugDetails().description());
            }

            /*
             * adding stack trace of failed test
             */
            if (tc.getResult().getThrowable() != null)
            {
                if (tc.getResult().getThrowable().getStackTrace() != null)
                {
                    StringWriter sw = new StringWriter();
                    tc.getResult().getThrowable().printStackTrace(new PrintWriter(sw));
                    result.put("comment", sw.toString());
                }
            }
            results.add(result);
        }

        data.put("results", results);

        LOG.info("TestRAILAPI: Bulk Upload [{} # total test cases] on Run: {} ", currentTestCases.size(), currentRun.getName());
        return postRequest("add_results_for_cases/" + currentRun.getId(), data);
    }

    @SuppressWarnings("unchecked")
    public Section addNewSection(String name, Object parent_id, int projectID, int suite_id)
    {
        Section s = new Section();

        @SuppressWarnings("rawtypes")
        Map data = new HashMap();
        data.put("suite_id", suite_id);
        data.put("name", name);
        data.put("parent_id", parent_id);

        if (parent_id == null)
        {
            LOG.info("Add missing root section [{}]:", name);
        }
        else
            LOG.info("Add missing section [{}] as child of parent section with ID: {}", name, parent_id);

        Object response;
        try
        {
            response = postRequest("add_section/" + projectID, data);
            s = toClass(response, Section.class);
        }
        catch (Exception e)
        {
            LOG.error("Cannot add new section: {}", e.getMessage());
        }
        return s;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public TestCase uploadTestCase(TestCaseDetail currentTest)
    {
        try
        {
            Map data = new HashMap();
            data.put("title", currentTest.getName());
            data.put("template_id", TEMPLATE_ID);
            data.put("type_id", currentTest.getTestRailAnnotation().testType().value());

            // /*
            // * Steps
            // */
            // @SuppressWarnings("rawtypes")
            // List steps = new ArrayList();
            //
            // @SuppressWarnings("rawtypes")
            // Map step1 = new HashMap();
            // step1.put("status_id", new Integer(1));
            // step1.put("content", "Step 1");
            // step1.put("expected", "desc2");
            // steps.add(step1);
            // data.put("custom_steps_separated", steps);
            // data.put("custom_steps", annotation.description());
            data.put("custom_auto_ref", currentTest.getId());
            data.put("custom_executiontype", new Boolean(true)); // always automated
            data.put("custom_test_notes", currentTest.getNotes());

            // holds Sanity, Smoke, Regression, etc
            List<Integer> executionTypeList = new ArrayList<Integer>();
            for (ExecutionType et : currentTest.getTestRailAnnotation().executionType())
            {
                executionTypeList.add(et.value());
            }

            data.put("custom_exce_type", executionTypeList);
            data.put("custom_description", currentTest.getTestRailAnnotation().description());
            data.put("priority_id", new Integer(TEST_PRIORITY_MEDIUM));
            data.put("custom_platform", 1);

            LOG.info("TestRAILAPI: Upload TestCase: {} to {}", currentTest.getId(), currentTest.getTestCaseDestination().toString());
            Object response = postRequest("add_case/" + currentTest.getTestCaseDestination().getDestination().getId(), data);
            return toClass(response, TestCase.class);
        }
        catch (Exception e)
        {
            LOG.error(e.getMessage());
        }

        return null;
    }

    public void createNewSection(TestCaseDetail currentTestCase, List<Section> allSections)
    {
        if (!currentTestCase.getTestCaseDestination().hasRootSection())
        {
            Section root = addNewSection(currentTestCase.getTestCaseDestination().getRootSectionName(), null, currentProjectID, currentSuiteID);
            currentTestCase.getTestCaseDestination().setRootSection(root);
            allSections.add(root);
        }

        int depth = 0;
        Section parent = currentTestCase.getTestCaseDestination().getRootSection();
        for (String childSection : currentTestCase.getTestCaseDestination().getChildDestinationSection())
        {
            depth += 1;
            if (!isSectionInList(depth, parent.getId(), childSection, allSections))
            {
                Section child = addNewSection(childSection, parent.getId(), currentProjectID, currentSuiteID);
                allSections.add(child);
                parent = child;
                currentTestCase.getTestCaseDestination().setDestination(child);
            }
        }

    }

    private boolean isSectionInList(int depth, int parent_id, String name, List<Section> allSections)
    {
        boolean exists = false;
        for (Section s : allSections)
        {
            if (s.getName().equals(name) && s.getDepth() == depth && s.getParent_id() == parent_id)
            {
                return true;
            }
        }
        return exists;
    }

}
