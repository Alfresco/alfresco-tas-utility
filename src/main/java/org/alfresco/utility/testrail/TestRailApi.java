package org.alfresco.utility.testrail;

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

import org.alfresco.utility.LogFactory;
import org.alfresco.utility.Utility;
import org.alfresco.utility.testrail.annotation.TestRail;
import org.alfresco.utility.testrail.model.Run;
import org.alfresco.utility.testrail.model.Section;
import org.alfresco.utility.testrail.model.TestCase;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.testng.ITestResult;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Basic implementation of interacting with Test Rail
 * 
 * @author Paul Brodner
 */
public class TestRailApi
{
    Logger LOG = LogFactory.getLogger();

    /*
     * Test Rail Template:
     * 1 - Test Case
     */
    private static final int TEMPLATE_ID = new Integer(1);
    private static final int TEST_PRIORITY_MEDIUM = 2;

    Properties testRailProperties = new Properties();

    private String username;
    private String password;
    private String endPointApiPath;
    private int currentProjectID;
    private String currentRun;
    private boolean configurationError = true;

    private TestCase tmpTestCase = null;

    /**
     * Setup configuration from property file
     */
    public TestRailApi()
    {
        InputStream defaultPropsInputStream = getClass().getClassLoader().getResourceAsStream("default.properties");
        if (defaultPropsInputStream != null)
        {
            try
            {
                testRailProperties.load(defaultPropsInputStream);
                this.username = testRailProperties.getProperty("testManagement.username");
                Utility.checkObjectIsInitialized(username, "username");

                this.password = testRailProperties.getProperty("testManagement.apiKey");
                Utility.checkObjectIsInitialized(password, "password");

                this.endPointApiPath = testRailProperties.getProperty("testManagement.endPoint") + "index.php?/api/v2/";
                Utility.checkObjectIsInitialized(endPointApiPath, "endPointApiPath");

                this.currentProjectID = Integer.parseInt(testRailProperties.getProperty("testManagement.project"));
                Utility.checkObjectIsInitialized(currentProjectID, "currentProjectID");

                this.currentRun = testRailProperties.getProperty("testManagement.testRun");
                Utility.checkObjectIsInitialized(currentRun, "currentRun");
                configurationError = false;
            }
            catch (Exception e)
            {
                LOG.error("Cannot initialize Test Management Setting from default.properties file");
            }
        }
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
     * Parse the response received and convert it to <classz> passed as parameter
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

    public boolean hasConfigurationErrors()
    {
        return configurationError;
    }

    protected Object getRequest(String path) throws Exception
    {
        URL endPointURL = new URL(endPointApiPath + path);
        HttpURLConnection conn = (HttpURLConnection) endPointURL.openConnection();
        conn.addRequestProperty("Content-Type", "application/json");

        conn.addRequestProperty("Authorization", "Basic " + DatatypeConverter.printBase64Binary(String.format("%s:%s", username, password).getBytes()));

        return parseRespose(conn);
    }

    protected Object postRequest(String path, Object data) throws Exception
    {
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
     * Parse the response returned.
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

    public List<Section> getSectionsOfCurrentProject()
    {
        return getSections(currentProjectID);
    }

    public Run getRun(String name, int projectID)
    {
        for (Run run : getRuns(projectID))
        {
            if (run.getName().equals(name))
                return run;
        }
        return null;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void addTestCase(ITestResult result, Section section, TestRail annotation)
    {
        try
        {
            Map data = new HashMap();
            data.put("title", result.getMethod().getMethodName());
            data.put("template_id", TEMPLATE_ID);
            data.put("type_id", annotation.testType().value());

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
            data.put("custom_auto_ref", getFullTestCaseName(result));
            data.put("custom_executiontype", new Boolean(true)); // always automated

            // holds Sanity, Smoke, Regression, etc
            List<Integer> executionTypeList = new ArrayList<Integer>();
            for (ExecutionType et : annotation.executionType())
            {
                executionTypeList.add(et.value());
            }

            data.put("custom_exce_type", executionTypeList);
            data.put("custom_expected", annotation.description());
            data.put("priority_id", new Integer(TEST_PRIORITY_MEDIUM));

            Object response = postRequest("add_case/" + section.getId(), data);
            tmpTestCase = toClass(response, TestCase.class);
        }
        catch (Exception e)
        {
            LOG.error(e.getMessage());
        }
    }

    public boolean isAutomatedTestCaseInSection(String testName, Section section, TestRail annotation)
    {
        /* index.php?/api/v2/get_cases/1&section_id=2&type_id=<custom> */
        tmpTestCase = null;
        try
        {
            Object response = getRequest("/get_cases/" + currentProjectID + "&type_id=" + annotation.testType().value() + "&section_id=" + section.getId());
            List<TestCase> existingTestCases = toCollection(response, TestCase.class);
            for (TestCase tc : existingTestCases)
            {
                if (tc.getTitle().equals(testName))
                {
                    tmpTestCase = tc;
                    return true;
                }
            }
        }
        catch (Exception e)
        {
            LOG.error(String.format("Cannot get test cases from Test Rail. Error %s", e.getMessage()));
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public void updateTestCaseResult(ITestResult result, Run run)
    {
        if (tmpTestCase == null)
            return;
        int status = 2; // blocked in Test Rail

        switch (result.getStatus())
        {
            case ITestResult.SUCCESS:
                status = 1; // Passed in Test Rail
                break;
            case ITestResult.FAILURE:
                status = 5; // Failed in Test Rail
                break;
            case ITestResult.SKIP:
                status = 4; // Retest in TestRail
                break;
            case ITestResult.SUCCESS_PERCENTAGE_FAILURE:
                status = 1;
                break;
            default:
                break;
        }

        @SuppressWarnings("rawtypes")
        Map data = new HashMap();
        data.put("status_id", status);

        /*
         * adding stack trace of failed test
         */
        if (result.getThrowable() != null)
        {
            if (result.getThrowable().getStackTrace() != null)
            {
                StringWriter sw = new StringWriter();
                result.getThrowable().printStackTrace(new PrintWriter(sw));
                data.put("comment", sw.toString());
            }
        }
        Object response = null;
        try
        {
            response = postRequest("add_result_for_case/" + run.getId() + "/" + tmpTestCase.getId(), data);
        }
        catch (Exception e)
        {
            LOG.error("Cannot update Test Case status execution. Error: {}, Response: {}", e.getMessage(), response.toString());
        }
    }

    /**
     * Returns the current Test Runs of current project
     */
    public Run getRunOfCurrentProject()
    {
        return getRun(currentRun, currentProjectID);
    }

    public String getFullTestCaseName(ITestResult result)
    {
        return String.format("%s#%s", result.getInstanceName(), result.getName());
    }

    public List<Section> getSections(int projectID)
    {
        LOG.info("Get all sections from Test Rail Project with id: {}", projectID);
        Object response;
        try
        {
            response = getRequest("get_sections/" + projectID);
            return toCollection(response, Section.class);
        }
        catch (Exception e)
        {
            LOG.error(e.getMessage());
        }
        return new ArrayList<Section>();
    }

    public List<Run> getRuns(int projectID)
    {
        Object response;
        LOG.info("Get all Runs from Test Rail Project with id: {}", projectID);
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
}
