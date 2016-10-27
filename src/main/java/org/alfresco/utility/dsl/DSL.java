package org.alfresco.utility.dsl;

import org.alfresco.utility.exception.TestConfigurationException;
import org.alfresco.utility.model.ContentModel;
import org.alfresco.utility.model.SiteModel;
import org.alfresco.utility.model.UserModel;

public interface DSL<Data>
{
    /**
     * Defines the current test user that will be used
     */
    Data usingUser(UserModel user) throws Exception;

    /**
     * Defines the current user to be Admin
     * 
     * @return
     * @throws Exception
     */
    Data usingAdmin() throws Exception;

    /**
     * gets the current test user
     * 
     * @return
     */
    UserModel getCurrentUser();

    /**
     * @return the rootPath
     * @throws Exception
     */
    Data usingRoot() throws Exception;

    /**
     * @return the current Repository Space
     * @throws TestConfigurationException
     */
    String getCurrentSpace() throws TestConfigurationException;

    /**
     * {@link #getSitesPath()}, {@linkplain #getUserHomesPath()}, {@link #getDataDictionaryPath()}, etc If nothing is
     * specified, the root folder is used
     */
    Data setCurrentSpace(String currentRepositorySpace);

    /**
     * Defines the current file/folder that is under test
     * 
     * @param model
     * @return
     * @throws Exception
     */
    Data usingResource(ContentModel model) throws Exception;

    /**
     * Defines the current test site to be used
     * 
     * @param siteId
     * @return
     * @throws Exception
     */
    Data usingSite(String siteId) throws Exception;

    /**
     * Defines the current test site to be used based on {@link SiteModel}
     * 
     * @param siteModel
     * @return
     * @throws Exception
     */
    Data usingSite(SiteModel siteModel) throws Exception;

    /**
     * Defines the current User Home to be used Example: /User Homes/<username>
     * 
     * @param username
     * @return
     * @throws Exception
     */
    Data usingUserHome(String username) throws Exception;

    /**
     * @return the userhome path based on {@link #getCurrentUser()}
     * @throws Exception
     */
    Data usingUserHome() throws Exception;

    /**
     * @return the root path
     * @throws TestConfigurationException
     */
    String getRootPath() throws TestConfigurationException;

    /**
     * @return the Sites path Example: /Sites
     * @throws TestConfigurationException
     */
    String getSitesPath() throws TestConfigurationException;

    /**
     * @return user home path Example: /User Homes
     * @throws TestConfigurationException
     */
    String getUserHomesPath() throws TestConfigurationException;

    /**
     * @return the Data Dictionary path /Data Dictionary
     * @throws TestConfigurationException
     */
    String getDataDictionaryPath() throws TestConfigurationException;
}
