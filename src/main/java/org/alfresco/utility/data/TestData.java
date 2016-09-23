package org.alfresco.utility.data;

import org.alfresco.utility.LogFactory;
import org.alfresco.utility.TasProperties;
import org.alfresco.utility.Utility;
import org.alfresco.utility.dsl.DSL;
import org.alfresco.utility.exception.TestConfigurationException;
import org.alfresco.utility.model.ContentModel;
import org.alfresco.utility.model.SiteModel;
import org.alfresco.utility.model.UserModel;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.io.Files;

public abstract class TestData<Data> implements DSL<Data>
{
    static Logger LOG = LogFactory.getLogger();

    /**
     * This is the currentUser that we will use for creating specific test data
     * If none specified we will use the admin user defined in
     * default.properties
     */
    private UserModel currentUser;

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

    @Autowired
    protected TasProperties tasProperties;

    public static String PASSWORD = "password";
    public static String EMAIL = "%s@tas-automation.org";

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
    public void setCurrentSpace(String currentRepositorySpace)
    {
        this.currentSpace = currentRepositorySpace;
    }

    /**
     * Resource can be a folder, file where we want to create new content or
     * make assertion. The resource is the current object tested in your test
     */
    @Override
    @SuppressWarnings("unchecked")
    public Data usingResource(ContentModel model) throws Exception
    {
        setLastResource(model.getName());
        return (Data) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Data usingSite(String siteId) throws Exception
    {
        setCurrentSpace(String.format(getSitesPath(), siteId));
        setCurrentSite(siteId);
        setLastResource(getCurrentSpace());
        return (Data) this;
    }

    @Override
    public Data usingSite(SiteModel siteModel) throws Exception
    {
        Utility.checkObjectIsInitialized(siteModel, "siteModel");
        return usingSite(siteModel.getId());
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
    public void setLastResource(String lastResource)
    {
        this.lastResource = lastResource;
    }

    public void setCurrentUser(UserModel testUser)
    {
        this.currentUser = testUser;
    }
}
