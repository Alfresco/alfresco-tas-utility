package org.alfresco.utility.testrail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
    Logger LOG = LogFactory.getLogger();
    private String username;
    private String password;
    private String endPointApiPath;
    private int currentProjectID;

    @SuppressWarnings("unused")
    private TestCase tmpTestCase = null;

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
            data.put("template_id", new Integer(1));
            data.put("type_id", annotation.type().value());
            data.put("priority_id", new Integer(TEST_PRIORITY_MEDIUM));

            postRequest("add_case/" + section.getId(), data);
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
            Object response = getRequest("/get_cases/" + currentProjectID + "&type_id=" + annotation.type().value() +"&section_id=" + section.getId());
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
            LOG.error(e.getMessage());
        }
        return false;
    }

    public void updateTestCaseResult(ITestResult result)
    {

    }
}
