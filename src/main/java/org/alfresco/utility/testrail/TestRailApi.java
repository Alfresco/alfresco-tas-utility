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

import javax.xml.bind.DatatypeConverter;

import org.alfresco.utility.LogFactory;
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

public class TestRailApi
{
    private static final int TEST_PRIORITY_MEDIUM = 2;
    /*
     * Test Rail Template:
     * 1 - Test Case (Text)
     * 2 - Test Case (Steps)
     * 3 - Exploratory Session
     */
    private static final int TEMPLATE_ID = new Integer(1);

    Logger LOG = LogFactory.getLogger();
    private String username;
    private String password;
    private String endPointApiPath;
    private int currentProjectID;

    private TestCase tmpTestCase = null;

    /**
     * Handle interaction with Test Rail API
     */
    public TestRailApi()
    {
        this.username = "work.paul.brodner@gmail.com";
        this.password = "vqcR/bWdRucRkpy3SUA1";
        this.endPointApiPath = "https://pauly.testrail.net/" + "index.php?/api/v2/";
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
        return getSections(1);
    }
    /*
     * 
     */
    public List<Section> getSections(int projectID)
    {
        currentProjectID = projectID;
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

    public Run getRun(String name, int projectID)
    {
        for (Run run : getRuns(projectID))
        {
            if (run.getName().equals(name))
                return run;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public void addTestCase(String testName, Section section, TestRail annotation)
    {
        try
        {
            /*
             * {
             * "title": "aa",
             * "template_id": 1, -> testcase
             * "type_id": 3, -> automated
             * "priority_id": 2 -> mediu
             * }
             */
            @SuppressWarnings("rawtypes")
            Map data = new HashMap();
            data.put("title", testName);
            data.put("template_id", TEMPLATE_ID);
            data.put("type_id", annotation.type().value());
            // data.put("custom_preconds", "asdasd");

            // /*
            // * Steps - works when TEMPLATE_ID=2
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
            data.put("custom_steps", annotation.description());
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
        // type_id=3 -> automated
        // index.php?/api/v2/get_cases/1&section_id=2&type_id=3
        tmpTestCase = null;
        try
        {
            Object response = getRequest("/get_cases/" + currentProjectID + "&type_id=" + annotation.type().value() + "&section_id=" + section.getId());
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
            LOG.error(String.format("Cannot get test cases from Test Rail: Error %s", e.getMessage()));
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public void updateTestCaseResult(ITestResult result, Run run)
    {
        if (tmpTestCase == null)
            return;
        /*
         * {
         * "status_id": 5,
         * "comment": "haha",
         * "elapsed": "40s",
         * "version": "5.0.1-snapshot"
         * }
         */
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
        // data.put("elapsed", + "22m");
        // data.put("version", "1.0");

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

        try
        {
            postRequest("add_result_for_case/" + run.getId() + "/" + tmpTestCase.getId(), data);
        }
        catch (Exception e)
        {
            LOG.error("Cannot update Test Case status execution. Error: {}", e.getMessage());
        }
    }

    public Run getRunOfCurrentProject()
    {
        return getRun("automation", 1);
    }
}
