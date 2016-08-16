package org.alfresco.utility.dsl;

import static org.alfresco.utility.Utility.checkObjectIsInitialized;

import java.io.File;
import java.nio.file.Paths;

import org.alfresco.utility.JmxClient;
import org.alfresco.utility.LogFactory;
import org.alfresco.utility.data.DataContent;
import org.alfresco.utility.data.LastContentModel;
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

    private String currentRepositorySpace = null;
    private LastContentModel lastContentModel = new LastContentModel(this);

    // HELPERS ----------------------------------------------------------

    public String getRootPath() throws TestConfigurationException
    {
        return String.format("%s/%s", getRepositoryPrefixPath(), "");
    }

    public String getSitesPath() throws TestConfigurationException
    {
        return String.format("%s/%s", getRepositoryPrefixPath(), "Sites");
    }

    public String getUserHomesPath() throws TestConfigurationException
    {
        return String.format("%s%s", getRepositoryPrefixPath(), "/User Homes");
    }

    public String getDataDictionaryPath() throws TestConfigurationException
    {
        return String.format("%s/%s", getRepositoryPrefixPath(), "Data Dictionary");
    }

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
    public String getCurrentRepositorySpace() throws TestConfigurationException
    {
        if (currentRepositorySpace == null)
            currentRepositorySpace = getRootPath();

        return currentRepositorySpace;
    }

    public void setCurrentRepositorySpace(String currentRepositorySpace)
    {
        this.currentRepositorySpace = currentRepositorySpace;
        setLastContentModelUsed(currentRepositorySpace);
    }

    public String getLastContentModelUsed()
    {
        return lastContentModel.getFullPath();
    }

    public void setLastContentModelUsed(String fullPath)
    {
        this.lastContentModel.setFullPath(fullPath);
    }

    public String getProtocolName()
    {
        return this.getClass().getSimpleName().replaceAll("Wrapper", "");
    }

    // DSL ----------------------------------------------------------
    public abstract Client authenticateUser(UserModel userModel) throws Exception;

    public abstract Client disconnect() throws Exception;

    /**
     * Just verify using JMX calls if the protocl is enabled on server or not
     */
    public void assertProtocolIsEnabled() throws Exception
    {
        Assert.assertTrue(isProtocolEnabled(), String.format("%s protocol is enabled", getProtocolName()));
    }

    @SuppressWarnings("unchecked")
    public Client usingSite(String siteId) throws Exception
    {
        checkObjectIsInitialized(siteId, "SiteID");
        setCurrentRepositorySpace(buildSiteDocumentLibraryPath(siteId, ""));
        return (Client) this;
    }

    @SuppressWarnings("unchecked")
    public Client usingRoot() throws Exception
    {
        setCurrentRepositorySpace(getRootPath());
        return (Client) this;
    }

    @SuppressWarnings("unchecked")
    public Client usingSite(SiteModel siteModel) throws Exception
    {
        checkObjectIsInitialized(siteModel, "SiteModel");
        String path = buildSiteDocumentLibraryPath(siteModel.getId(), "");
        setCurrentRepositorySpace(path);
        return (Client) this;
    }

    @SuppressWarnings("unchecked")
    public Client usingUserHome(String username) throws Exception
    {
        checkObjectIsInitialized(username, "username");
        setCurrentRepositorySpace(buildUserHomePath(username, ""));
        return (Client) this;
    }

    @SuppressWarnings("unchecked")
    public Client usingContent(ContentModel model) throws Exception
    {
        usingContent(model.getLocation());
        return (Client) this;
    }

    /**
     * Operations on files or folders
     * If you call this method you can use all assertion within this wrapper
     * 
     * @param contentName
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Client usingContent(String contentName) throws Exception
    {
        checkObjectIsInitialized(contentName, "contentName");
        setCurrentRepositorySpace(String.format("%s%s/", getCurrentRepositorySpace(), contentName));
        return (Client) this;
    }

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

    // ASSERTIONS ----------------------------------------------------------
    @SuppressWarnings("unchecked")
    public Client assertExistsInRepo()
    {
        dataContent.assertContentExist(getLastContentModelUsed());
        return (Client) this;
    }

    @SuppressWarnings("unchecked")
    public Client assertDoesNotExistInRepo()
    {
        dataContent.assertContentDoesNotExist(getLastContentModelUsed());
        return (Client) this;
    }

    /**
     * Check for content in repository
     * Just pass in the contentName that you are looking for
     * This assertion works well if you are using first {@link #usingContent(ContentModel)}
     * {@link #usingRoot()}, {@link #usingSite(String)}, etc prefixed with <using> keyword
     * 
     * @param contentName
     * @return
     */
    @SuppressWarnings("unchecked")
    public Client assertContentExist(String contentName)
    {
        if (TestData.isAFile(getLastContentModelUsed()))
        {
            String useParent = new File(getLastContentModelUsed()).getParentFile().toString();
            dataContent.assertContentExist(Paths.get(useParent, contentName).toString());
        }
        else
        {
            dataContent.assertContentExist(Paths.get(getLastContentModelUsed(), contentName).toString());
        }

        return (Client) this;
    }

    @SuppressWarnings("unchecked")
    public Client assertContentDoesNotExist(String contentName)
    {
        dataContent.assertContentDoesNotExist(Paths.get(getLastContentModelUsed(), contentName).toString());
        return (Client) this;
    }

}
