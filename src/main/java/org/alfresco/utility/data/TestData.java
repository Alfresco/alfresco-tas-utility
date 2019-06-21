package org.alfresco.utility.data;

import static org.alfresco.utility.report.log.Step.STEP;

import java.util.HashMap;
import java.util.Map;

import javax.management.openmbean.CompositeData;

import org.alfresco.dataprep.AlfrescoHttpClient;
import org.alfresco.dataprep.AlfrescoHttpClientFactory;
import org.alfresco.utility.LogFactory;
import org.alfresco.utility.TasProperties;
import org.alfresco.utility.Utility;
import org.alfresco.utility.dsl.DSL;
import org.alfresco.utility.dsl.DSLJmx;
import org.alfresco.utility.exception.TestConfigurationException;
import org.alfresco.utility.model.ContentModel;
import org.alfresco.utility.model.FileModel;
import org.alfresco.utility.model.FolderModel;
import org.alfresco.utility.model.SiteModel;
import org.alfresco.utility.model.UserModel;
import org.alfresco.utility.network.JmxBuilder;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.CharEncoding;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;

import com.google.common.io.Files;

public abstract class TestData<Data> implements DSL<Data>
{
    static Logger LOG = LogFactory.getLogger();

    /**
     * This is the currentUser that we will use for creating specific test data
     * If none specified we will use the admin user defined in
     * default.properties
     */
    protected UserModel currentUser;

    /**
     * The current space (location) used in Test Data This can be a Site
     * location a User Home location, a Data Dictionary location
     */
    private String currentSpace = "/";

    /**
     * The current site used in test
     */
    private String currentSite = "";

    /**
     * The last content used when calling {@link #usingResource(String)} method
     */
    private String lastResource = "";

    /**
     * The last content node id used
     */
    private String lastNodeId = "";

    @Autowired
    protected TasProperties tasProperties;

    @Autowired
    private AlfrescoHttpClientFactory alfrescoHttpClientFactory;

    public static String PASSWORD = "password";
    public static String EMAIL = "%s@tas-automation.org";

    private String serverLogUrl;

    private String logResponse;

    @Autowired
    private JmxBuilder jmxBuilder;

    /**
     * Check if <filename> passed as parameter is a file or not based on
     * extension
     */
    public static boolean isAFile(String filename)
    {
        return Files.getFileExtension(filename).length() == 3;
    }

    /**
     * Call this method if you want to use another user rather than admin
     * defined in {@link TasProperties} properties file
     * 
     * @param user
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    public Data usingUser(UserModel user)
    {
        currentUser = user;
        return (Data) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Data usingAdmin()
    {
        currentUser = getAdminUser();
        return (Data) this;
    }

    public UserModel getAdminUser()
    {
        return new UserModel(tasProperties.getAdminUser(), tasProperties.getAdminPassword());
    }

    @Override
    public UserModel getCurrentUser()
    {
        if (currentUser == null)
        {
            usingAdmin();
        }
        return currentUser;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Data usingRoot() throws Exception
    {
        setCurrentSpace("");
        return (Data) this;
    }

    @Override
    public String getCurrentSpace()
    {
        return currentSpace;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Data setCurrentSpace(String currentRepositorySpace)
    {
        this.currentSpace = currentRepositorySpace;
        return (Data) this;
    }

    /**
     * Resource can be a folder, file where we want to create new content or
     * make assertion. The resource is the current object tested in your test
     */
    @Override
    @SuppressWarnings("unchecked")
    public Data usingResource(ContentModel model) throws Exception
    {
        if (model.getCmisLocation().equals(model.getName()))
        {
            String location = "";
            if (model instanceof FolderModel)
            {
                location = Utility.buildPath(getLastResource(), model.getName());
            }
            else if (model instanceof FileModel)
            {
                FileModel fileModel = (FileModel) model;
                if(FilenameUtils.getExtension(model.getName()).length() == 0)
                {
                    location = Utility.buildPath(getLastResource(), String.format("%s.%s", model.getName(), fileModel.getFileType().extension));
                }
                else
                {
                    location = Utility.buildPath(getLastResource(), model.getName());
                }
            }

            location = Utility.removeLastSlash(location);
            model.setCmisLocation(location);
        }
        setLastResource(model.getCmisLocation());
        setCurrentSpace(model.getCmisLocation());
        setLastNodeId(model.getNodeRefWithoutVersion());
        return (Data) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Data usingSite(SiteModel siteModel) throws Exception
    {
        Utility.checkObjectIsInitialized(siteModel, "siteModel");

        setCurrentSpace(String.format(getSitesPath(), siteModel.getId()));
        setCurrentSite(siteModel.getId());
        setLastResource(getCurrentSpace());
        setLastNodeId(getSiteDocLibNodeId(siteModel));
        return (Data) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Data usingUserHome(String username) throws Exception
    {
        setCurrentSpace(String.format(getUserHomesPath(), username));
        return (Data) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Data usingUserHome() throws Exception
    {
        setCurrentSpace(String.format(getUserHomesPath(), getCurrentUser().getUsername()));
        return (Data) this;
    }

    @Override
    public String getRootPath() throws TestConfigurationException
    {
        return "";
    }

    @Override
    public String getSitesPath() throws TestConfigurationException
    {
        return "/Sites/%s/documentLibrary";
    }

    @Override
    public String getUserHomesPath() throws TestConfigurationException
    {
        return "/User Homes/%s";
    }

    @Override
    public String getDataDictionaryPath() throws TestConfigurationException
    {
        return "/Data Dictionary";
    }

    public String getLastNodeId()
    {
        return lastNodeId;
    }

    public void setLastNodeId(String lastNodeId)
    {
        this.lastNodeId = lastNodeId;
    }

    public String getCurrentSite()
    {
        return currentSite;
    }

    public void setCurrentSite(String currentSite)
    {
        this.currentSite = currentSite;
    }

    public String getLastResource()
    {
        return lastResource;
    }

    /**
     * Set last resource with content protocol location
     * 
     * @param lastResource
     */
    @SuppressWarnings("unchecked")
    public Data setLastResource(String lastResource)
    {
        this.lastResource = lastResource;
        return (Data) this;
    }

    public void setCurrentUser(UserModel testUser)
    {
        this.currentUser = testUser;
    }

    @SuppressWarnings("unchecked")
    public Data usingLastServerLogLines(int lineNumber) throws Exception
    {
        assertExtensionAmpExists("alfresco-log-extension");

        this.serverLogUrl = tasProperties.getFullServerUrl() + "/alfresco/s/tas/log";

        String baseDir = getAlfrescoHome();
        String logFile = (String) jmxBuilder.getJmxClient().readProperty("log4j:appender=File", "file");
        STEP(String.format("Log API: jmx log4j:appender=File", logFile));

        String logPath = logFile.contains(baseDir) ? logFile : Utility.buildPath(baseDir, logFile);
        STEP(String.format("Log API: log path is %s", logPath));

        Map<String, String> paramsServerlog = new HashMap<String, String>();
        paramsServerlog.put("path", logPath);
        paramsServerlog.put("lineNumber", "" + lineNumber);
        serverLogUrl = Utility.toUrlParams(serverLogUrl, paramsServerlog);

        HttpClient client = new HttpClient();
        GetMethod get = new GetMethod(serverLogUrl);
        String unhashedString = String.format("%s:%s", tasProperties.getAdminUser(), tasProperties.getAdminPassword());
        get.setRequestHeader("Authorization", "Basic " + Base64.encodeBase64String(unhashedString.getBytes()));

        get.getParams().setSoTimeout(5000);
        client.executeMethod(get);
        logResponse = IOUtils.toString(get.getResponseBodyAsStream(), CharEncoding.UTF_8);
        return (Data) this;
    }

    @SuppressWarnings("unchecked")
    public Data assertLogLineIs(String logLine) throws Exception
    {
        STEP(String.format("Log API: Assert that log file contains %s", logLine));
        Assert.assertTrue(logResponse.contains(logLine), String.format("Log file doesn't contain %s. Found %s", logLine, logResponse));
        return (Data) this;
    }

    public void assertExtensionAmpExists(String moduleId) throws Exception
    {
        boolean findModule = false;
        if (tasProperties.useJolokiaJmxAgent())
        {
            String allInstaledModules = (String) jmxBuilder.getJmxClient().readProperty("Alfresco:Name=ModuleService", "AllModules");
            JSONArray modules = new JSONArray(allInstaledModules);
            for (int i = 0; i < modules.length(); i++)
            {
                if (modules.getJSONObject(i).get("module.id").toString().equals(moduleId)
                        && modules.getJSONObject(i).get("module.installState").toString().equals("INSTALLED"))
                {
                    findModule = true;
                    break;
                }
            }
        }

        else
        {
            CompositeData[] allInstaledModules = (CompositeData[]) jmxBuilder.getJmxClient().readProperty("Alfresco:Name=ModuleService", "AllModules");
            for (CompositeData compData : allInstaledModules)
            {
                if ((compData.containsKey("module.id")) && (compData.get("module.id").equals(moduleId)))
                {
                    findModule = true;
                    break;
                }
            }
        }
        Assert.assertEquals(findModule, true, "Alfresco AMP module :" + moduleId + " is not installed");
    }
    
    /**
     * Returns Alfresco Content Services Home (Alfresco root) directory using JMX property
     * 
     * @return Alfresco Content Services Home (Alfresco root) directory
     * @throws Exception
     */
    public String getAlfrescoHome() throws Exception
    {
        String alfrescoHome = (String) jmxBuilder.getJmxClient().readProperty("Alfresco:Name=SystemProperties", "alfresco.home");
        STEP(String.format("Log API: jmx alfresco.home" + alfrescoHome));
        return alfrescoHome;
    }
    
    /**
     * @return JMX DSL for this wrapper
     */
    public DSLJmx withJMX()
    {
        return new DSLJmx(jmxBuilder.getJmxClient());
    }

    /**
     * Returns doclib node id for a given site by parsing the site's children nodes
     *
     * @return NodeId
     * @param site
     * @throws Exception
     */
    private String getSiteDocLibNodeId(SiteModel site)
    {
        String docLibId = null;
        AlfrescoHttpClient client = this.alfrescoHttpClientFactory.getObject();
        String reqUrl = client.getApiVersionUrl() + "nodes/" + site.getGuidWithoutVersion() + "/children";

        HttpGet get = new HttpGet(reqUrl);
        HttpResponse response = client.execute(tasProperties.getAdminUser(), tasProperties.getAdminPassword(), get);

        if (200 == response.getStatusLine().getStatusCode())
        {
            JSONObject jsonObject = new JSONObject(client.readStream(response.getEntity()));
            JSONArray children = jsonObject.getJSONObject("list").getJSONArray("entries");
            for (int i = 0; i < children.length(); i++)
            {
                JSONObject jsonObj = children.getJSONObject(i).getJSONObject("entry");
                if (jsonObj.getString("name").equals("documentLibrary"))
                {
                    docLibId = jsonObj.get("id").toString();
                }
            }
        }
        else
        {
            throw new RuntimeException(
                    "Could not set Doclib nodeId. Request response: " + client.getParameterFromJSON(response, "briefSummary", new String[] { "error" }));
        }
        return docLibId;
    }
}