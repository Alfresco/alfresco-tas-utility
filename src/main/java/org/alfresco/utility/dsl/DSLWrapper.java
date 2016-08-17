package org.alfresco.utility.dsl;

import java.io.File;
import java.nio.file.Paths;

import org.alfresco.utility.JmxClient;
import org.alfresco.utility.LogFactory;
import org.alfresco.utility.data.DataContent;
import org.alfresco.utility.data.DataValue;
import org.alfresco.utility.data.ResourceContent;
import org.alfresco.utility.data.TestData;
import org.alfresco.utility.exception.JmxException;
import org.alfresco.utility.exception.TestConfigurationException;
import org.alfresco.utility.model.ContentModel;
import org.alfresco.utility.model.SiteModel;
import org.alfresco.utility.model.UserModel;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;

/**
 * Wrapper on Client
 *
 * @param <Client>
 */
public abstract class DSLWrapper<Client> implements DSLEndPoint
{

    @Autowired
    DataContent dataContent;

    @Autowired
    protected JmxClient jmxClient;

    protected Logger LOG = LogFactory.getLogger();

    /**
     * Will keep track of the last space used in test.
     * Space is for example the path of the /Sites/siteId/documentLibrary
     * or the location of /User Homes/tester and so on
     */
    private String currentSpace = null;

    /**
     * lastResource will keep track of the last {@link ContentModel} created, so file/folders created via each protocols
     */
    private ResourceContent lastResource = new ResourceContent(this);

    /**
     * This is the test user used in test
     * It should be defined in the {@link #authenticateUser(UserModel)} method
     */
    private UserModel testUser = new UserModel(DataValue.UNDEFINED.toString(), DataValue.UNDEFINED.toString());

    // HELPERS ----------------------------------------------------------
    /**
     * get the current status true/false of the protocol on test server
     * 
     * @return
     * @throws Exception
     */
    protected abstract String getProtocolJMXConfigurationStatus() throws Exception;

    /**
     * @return true/false if current protocl is enabled on server
     * @throws Exception
     */
    public boolean isProtocolEnabled() throws Exception
    {
        if (!jmxClient.isJMXEnabled())
            throw new JmxException("JMX not enabled on server");

        LOG.info("Check [{}] protocol is enabled via JMX calls", getProtocolName());

        String status = getProtocolJMXConfigurationStatus();

        jmxClient.closeConnection();
        return status.equals("true");
    }

    /**
     * Helper for building strings of the resource passed as parameter
     * 
     * @param parent
     * @param paths
     * @return concatenated paths of <parent> + each <paths>
     */
    protected String buildPath(String parent, String... paths)
    {
        StringBuilder concatenatedPaths = new StringBuilder(parent);
        for (String path : paths)
        {
            concatenatedPaths.append(path);
            concatenatedPaths.append("/");
        }
        return concatenatedPaths.toString();
    }

    /**
     * @param siteId
     * @param filesOrFoldersHierarcy
     * @return the full path of <filesOrFoldersHierarcy> inside /Sites/documentLibrary
     * @throws TestConfigurationException
     */
    protected String buildSiteDocumentLibraryPath(String siteId, String... filesOrFoldersHierarcy) throws TestConfigurationException
    {
        String root = String.format("%s/%s/%s", getSitesPath(), siteId, "documentLibrary");
        return buildPath(root, filesOrFoldersHierarcy);
    }

    /**
     * @param siteId
     * @param filesOrFoldersHierarcy
     * @return the full path of <filesOrFoldersHierarcy> inside /User Home/<username>
     * @throws TestConfigurationException
     */
    protected String buildUserHomePath(String username, String... filesOrFoldersHierarcy) throws TestConfigurationException
    {
        String root = String.format("%s/%s", getUserHomesPath(), username);
        return buildPath(root, filesOrFoldersHierarcy);
    }

    /**
     * @return the current Repository Space, can be: getSitesPath(), getRootPath(), getUserHomesPath(), etc
     *         If nothing is specified, the root folder is used
     * @throws TestConfigurationException
     */
    @Override
    public String getCurrentSpace() throws TestConfigurationException
    {
        if (currentSpace == null)
            currentSpace = getRootPath();

        return currentSpace;
    }

    public void setCurrentSpace(String currentRepositorySpace)
    {
        this.currentSpace = currentRepositorySpace;
        setLastResource(currentRepositorySpace);
    }

    public String getLastResource()
    {
        return lastResource.getFullPath();
    }

    public void setLastResource(String fullPath)
    {
        this.lastResource.setFullPath(fullPath);
    }

    /**
     * @return the name of the protocol based on the class name
     */
    public String getProtocolName()
    {
        return this.getClass().getSimpleName().replaceAll("Wrapper", "");
    }

    /**
     * @return test user. This should be defined in {@link #authenticateUser(UserModel)} method
     */
    public UserModel getTestUser()
    {
        return testUser;
    }

    /**
     * Define the test user.
     * This should be initialized in {@link #authenticateUser(UserModel)} method
     * 
     * @param testUser
     */
    public void setTestUser(UserModel testUser)
    {
        this.testUser = testUser;
    }

    /*
     * DSL ----------------------------------------------------------
     */
    public abstract Client authenticateUser(UserModel userModel) throws Exception;

    public abstract Client disconnect() throws Exception;

    public abstract String getRootPath() throws TestConfigurationException;

    public abstract String getSitesPath() throws TestConfigurationException;

    public abstract String getUserHomesPath() throws TestConfigurationException;

    public abstract String getDataDictionaryPath() throws TestConfigurationException;

    /**
     * Just verify using JMX calls if the protocl is enabled on server or not
     */
    public void assertProtocolIsEnabled() throws Exception
    {
        Assert.assertTrue(isProtocolEnabled(), String.format("%s protocol is enabled", getProtocolName()));
    }

    @SuppressWarnings("unchecked")
    public Client usingRoot() throws Exception
    {
        setCurrentSpace(getRootPath());
        return (Client) this;
    }

    /**
     * User for changing current site location
     * This method will build the path of the siteId location
     * Example: /Sites/<siteId>/documentLibrary
     * Add implementation for your protocol accordingly
     * 
     * @param siteId
     * @return
     * @throws Exception
     */
    public abstract Client usingSite(String siteId) throws Exception;

    public abstract Client usingSite(SiteModel siteModel) throws Exception;

    public abstract Client usingUserHome(String username) throws Exception;

    public abstract Client usingUserHome() throws Exception;

    /**
     * Operations on files or folders
     * If you call this method you can use all assertion within this wrapper
     * 
     * @param model
     * @return
     * @throws Exception
     */
    public abstract Client usingResource(ContentModel model) throws Exception;

    /**
     * Operations on files or folders
     * If you call this method you can use all assertion within this wrapper
     * 
     * @param contentName
     * @return
     * @throws Exception
     */
    public abstract Client usingResource(String contentName) throws Exception;

    @SuppressWarnings("unchecked")
    public Client and()
    {
        return (Client) this;
    }

    @SuppressWarnings("unchecked")
    public Client then()
    {
        return (Client) this;
    }

    @SuppressWarnings("unchecked")
    public Client when()
    {
        return (Client) this;
    }

    /*
     * ASSERTIONS ----------------------------------------------------------
     */

    @SuppressWarnings("unchecked")
    public Client assertExistsInRepo()
    {
        dataContent.assertContentExist(getLastResource(), getTestUser());
        return (Client) this;
    }

    @SuppressWarnings("unchecked")
    public Client assertDoesNotExistInRepo()
    {
        dataContent.assertContentDoesNotExist(getLastResource(), getTestUser());
        return (Client) this;
    }

    /**
     * Check for content in repository
     * Just pass in the contentName that you are looking for
     * This assertion works well if you are using first {@link #usingResource(ContentModel)}
     * {@link #usingRoot()}, {@link #usingSite(String)}, etc prefixed with <using> keyword
     * 
     * @param contentName
     * @return
     */
    @SuppressWarnings("unchecked")
    public Client assertContentExist(String contentName)
    {
        if (TestData.isAFile(getLastResource()))
        {
            String useParent = new File(getLastResource()).getParentFile().toString();
            dataContent.assertContentExist(Paths.get(useParent, contentName).toString(), getTestUser());
        }
        else
        {
            dataContent.assertContentExist(Paths.get(getLastResource(), contentName).toString(), getTestUser());
        }

        return (Client) this;
    }

    @SuppressWarnings("unchecked")
    public Client assertContentDoesNotExist(String contentName)
    {
        dataContent.assertContentDoesNotExist(Paths.get(getLastResource(), contentName).toString(), getTestUser());
        return (Client) this;
    }
}
