package org.alfresco.utility.dsl;

import static org.alfresco.utility.report.log.Step.STEP;

import java.io.IOException;

import org.alfresco.utility.Utility;
import org.alfresco.utility.data.ResourceContent;
import org.alfresco.utility.exception.TestConfigurationException;
import org.alfresco.utility.model.ContentModel;
import org.alfresco.utility.model.SiteModel;
import org.alfresco.utility.model.UserModel;
import org.alfresco.utility.network.JmxClient;

/**
 * Extend this class if you want add implementation for a new protocol.
 * See example of CifsWrapper, FtpWrapper
 *
 * @param <Client>
 */
public abstract class DSLProtocol<Client> extends DSLWrapper<Client> implements DSL<Client>
{
    /**
     * Will keep track of the last space used in test. Space is for example the
     * path of the /Sites/siteId/documentLibrary or the location of /User
     * Homes/tester and so on
     */
    private String currentSpace = null;

    /**
     * lastResource will keep track of the last {@link ContentModel} created, so
     * file/folders created via each protocols
     */
    private ResourceContent lastResource = new ResourceContent(this);
    private ContentModel lastContentModel = new ContentModel();
    
    @Override
    @SuppressWarnings("unchecked")
    public Client usingUser(UserModel user) throws Exception
    {
        setTestUser(user);
        return (Client) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Client usingAdmin() throws Exception
    {
        setTestUser(dataContent.getAdminUser());
        return (Client) this;
    }

    @Override
    public UserModel getCurrentUser()
    {
        return getTestUser();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Client usingRoot() throws Exception
    {
        setCurrentSpace(getRootPath());
        return (Client) this;
    }

    /**
     * @param siteId
     * @param filesOrFoldersHierarcy
     * @return the full path of <filesOrFoldersHierarcy> inside
     *         /Sites/documentLibrary
     * @throws TestConfigurationException
     */
    protected String buildSiteDocumentLibraryPath(String siteId, String... filesOrFoldersHierarcy) throws TestConfigurationException
    {
        String root = String.format("%s/%s/%s", getSitesPath(), siteId, "documentLibrary");
        return Utility.buildPath(root, filesOrFoldersHierarcy);
    }

    /**
     * @param siteId
     * @param filesOrFoldersHierarcy
     * @return the full path of <filesOrFoldersHierarcy> inside /User Home/
     *         <username>
     * @throws TestConfigurationException
     */
    protected String buildUserHomePath(String username, String... filesOrFoldersHierarcy) throws TestConfigurationException
    {
        String root = String.format("%s/%s", getUserHomesPath(), username);
        return Utility.buildPath(root, filesOrFoldersHierarcy);
    }

    /**
     * @return the current Repository Space, can be: getSitesPath(),
     *         getRootPath(), getUserHomesPath(), etc If nothing is specified,
     *         the root folder is used
     * @throws TestConfigurationException
     */
    @Override
    public String getCurrentSpace() throws TestConfigurationException
    {
        if (currentSpace == null)
            currentSpace = getRootPath();
        return currentSpace;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Client setCurrentSpace(String currentRepositorySpace)
    {
        this.currentSpace = currentRepositorySpace;
        setLastResource(currentRepositorySpace);
        return (Client) this;
    }

    @SuppressWarnings("unchecked")
    public Client closeJmxConnection() throws IOException
    {
        if (jmxBuilder.getJmxClient() instanceof JmxClient)
        {
            JmxClient jmx = (JmxClient) jmxBuilder.getJmxClient();
            jmx.closeConnection();
        }
        return (Client) this;
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
        LOG.info("Check [{}] protocol is enabled via JMX", getProtocolName());
        String status = getProtocolJMXConfigurationStatus();        
        return status.equals("true");
    }

    /**
     * Returns the last ContentModel resource created within alfresco repository
     * This will return the full path so something like:
     * or ->
     * smb://172.29.100.215/alfresco/Sites/qcAqgLSMO2OU5txtPMQG/documentLibrary/
     * /folder-syKFUjMWgY if CIFS protocol is used
     */
    public String getLastResource()
    {
        return lastResource.getFullPath();
    }

    /**
     * The same as {@link #getLastResource()} but without the repository prefix
     * So if the lastResource is
     * smb://172.29.100.215/alfresco/Sites/qcAqgLSMO2OU5txtPMQG/documentLibrary/
     * folder-syKFUjMWgY we will return only
     * "/Sites/qcAqgLSMO2OU5txtPMQG/documentLibrary//folder-syKFUjMWgY" without
     * "smb://172.29.100.215/alfresco" prefix defined in {@link #getPrefixSpace()} method
     */
    public String getLastResourceWithoutPrefix()
    {
        return lastResource.getPathWithoutPrefix();
    }

    /**
     * The last resource should be the full path of the last file/folder used in
     * tests
     * 
     * @param fullPath
     */
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

    /*
     * DSL --------------------------------------------
     */

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

    public abstract Client authenticateUser(UserModel userModel) throws Exception;

    public abstract Client disconnect() throws Exception;

    /**
     * User for changing current site location This method will build the path
     * of the siteId location Example: /Sites/<siteId>/documentLibrary Add
     * implementation for your protocol accordingly
     * 
     * @param siteId
     * @return
     * @throws Exception
     */
    @Override
    public abstract Client usingSite(String siteId) throws Exception;

    @Override
    public abstract Client usingSite(SiteModel siteModel) throws Exception;

    @Override
    public abstract Client usingUserHome(String username) throws Exception;

    @Override
    public abstract Client usingUserHome() throws Exception;

    @Override
    public abstract String getRootPath() throws TestConfigurationException;

    @Override
    public abstract String getSitesPath() throws TestConfigurationException;

    @Override
    public abstract String getUserHomesPath() throws TestConfigurationException;

    @Override
    public abstract String getDataDictionaryPath() throws TestConfigurationException;

    public abstract String getPrefixSpace();

    /**
     * Operations on files or folders If you call this method you can use all
     * assertion within this wrapper
     * 
     * @param model
     * @return
     * @throws Exception
     */
    @Override
    public abstract Client usingResource(ContentModel model) throws Exception;

    /*
     * ASSERTIONS ----------------------------------------------------------
     */

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public DSLAssertion assertThat()
    {
        return new DSLAssertion(this);
    }
   
    @SuppressWarnings("unchecked")
    public Client waitSeconds(int seconds)
    {
        STEP(String.format("UTILITY: Waiting for %s seconds", seconds));
        Utility.waitToLoopTime(seconds);
        return (Client) this;
    }

    public String buildPath(String parent, String... paths)
    {
        return Utility.convertBackslashToSlash(Utility.buildPath(parent, paths)).replace("//", "/");
    }
    
    public ContentModel getLastContentModel()
    {
        return lastContentModel;
    }

    /**
     * The last ContentModel tested from {@link #usingResource(ContentModel)}
     * @param lastContentModel
     */
    public void setLastContentModel(ContentModel lastContentModel)
    {
        this.lastContentModel = lastContentModel;
    }    
}
