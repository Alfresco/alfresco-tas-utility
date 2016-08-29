package org.alfresco.utility.dsl;

import java.io.IOException;

import org.alfresco.utility.data.ResourceContent;
import org.alfresco.utility.exception.JmxException;
import org.alfresco.utility.exception.TestConfigurationException;
import org.alfresco.utility.model.ContentModel;
import org.alfresco.utility.model.SiteModel;
import org.alfresco.utility.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;

/**
 * Extend this class if you want add implementation for a new protocol.
 * 
 * See example of CifsWrapper, FtpWrapper
 *
 * @param <Client>
 */
public abstract class DSLProtocol<Client> extends DSLWrapper<Client>
{

    /**
     * lastResource will keep track of the last {@link ContentModel} created, so file/folders created via each protocols
     */
    private ResourceContent lastResource = new ResourceContent(this);

    @SuppressWarnings("unchecked")
    public Client usingRoot() throws Exception
    {
        setCurrentSpace(getRootPath());
        return (Client) this;
    }

    /**
     * Will keep track of the last space used in test.
     * Space is for example the path of the /Sites/siteId/documentLibrary
     * or the location of /User Homes/tester and so on
     */
    private String currentSpace = null;

    /**
     * Helper for building strings of the resource passed as parameter
     * 
     * @param parent
     * @param paths
     * @return concatenated paths of <parent> + each <paths>
     */
    public String buildPath(String parent, String... paths)
    {
       StringBuilder concatenatedPaths = new StringBuilder(parent);
       int lenPaths = paths.length;
       if(lenPaths == 0)
           return concatenatedPaths.toString();
       
       if(!parent.endsWith("/"))
           concatenatedPaths.append("/");
       
        for (String path : paths)
        {
            if(!path.isEmpty())
            {
                concatenatedPaths.append(path);
                concatenatedPaths.append("/");    
            }            
        }
        String concatenated = concatenatedPaths.toString();
        if(lenPaths > 0  && paths[lenPaths-1].contains("."))
            concatenated = StringUtils.removeEnd(concatenated, "/");
        return concatenated;
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
    
    @SuppressWarnings("unchecked")
    public Client closeJmxConnection() throws IOException
    {
        jmxClient.closeConnection();

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
        if (!jmxClient.isJMXEnabled())
            throw new JmxException("JMX not enabled on server");

        LOG.info("Check [{}] protocol is enabled via JMX calls", getProtocolName());

        String status = getProtocolJMXConfigurationStatus();

        return status.equals("true");
    }

    /**
     * Returns the last ContentModel resource created within alfresco repository
     * This will return the full path so something like:  
     *  
     *  or -> smb://172.29.100.215/alfresco/Sites/qcAqgLSMO2OU5txtPMQG/documentLibrary//folder-syKFUjMWgY 
     *  if CIFS protocol is used
     */
    public String getLastResource()
    {
        return lastResource.getFullPath();
    }
    
    /**
     * The same as {@link #getLastResource()} but without the repository prefix
     * So if the lastResource is smb://172.29.100.215/alfresco/Sites/qcAqgLSMO2OU5txtPMQG/documentLibrary/folder-syKFUjMWgY
     * we will return only "/Sites/qcAqgLSMO2OU5txtPMQG/documentLibrary//folder-syKFUjMWgY" without "smb://172.29.100.215/alfresco" 
     * prefix defined in {@link #getPrefixSpace()} method
     * 
     */
    public String getLastResourceWithoutPrefix()
    {
        return lastResource.getPathWithoutPrefix();
    }

    /**
     * The last resource should be the full path of the last file/folder used in tests
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
    public abstract Client authenticateUser(UserModel userModel) throws Exception;

    public abstract Client disconnect() throws Exception;

    /**
     * Operations on files or folders
     * If you call this method you can use all assertion within this wrapper
     * 
     * @param contentName
     * @return
     * @throws Exception
     */
    public abstract Client usingResource(String contentName) throws Exception;

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
    
    public abstract String getRootPath() throws TestConfigurationException;

    public abstract String getSitesPath() throws TestConfigurationException;

    public abstract String getUserHomesPath() throws TestConfigurationException;

    public abstract String getDataDictionaryPath() throws TestConfigurationException;
    
    public abstract String getPrefixSpace();
    
    /**
     * Operations on files or folders
     * If you call this method you can use all assertion within this wrapper
     * 
     * @param model
     * @return
     * @throws Exception
     */
    public abstract Client usingResource(ContentModel model) throws Exception;

    /*
     * ASSERTIONS ----------------------------------------------------------
     */

    @SuppressWarnings("unchecked")
    public Client assertExistsInRepo()
    {
        dataContent.assertContentExist(getLastResourceWithoutPrefix(), getTestUser());
        return (Client) this;
    }

    @SuppressWarnings("unchecked")
    public Client assertDoesNotExistInRepo()
    {
        dataContent.assertContentDoesNotExist(getLastResourceWithoutPrefix(), getTestUser());
        return (Client) this;
    }

    /**
     * Just verify using JMX calls if the protocol is enabled on server or not
     */
    public void assertProtocolIsEnabled() throws Exception
    {
        Assert.assertTrue(isProtocolEnabled(), String.format("%s protocol is enabled", getProtocolName()));
    }
}
