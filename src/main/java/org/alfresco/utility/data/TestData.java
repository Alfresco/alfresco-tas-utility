package org.alfresco.utility.data;

import org.alfresco.utility.LogFactory;
import org.alfresco.utility.TasProperties;
import org.alfresco.utility.model.UserModel;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.io.Files;

public abstract class TestData<Data>
{
    static Logger LOG = LogFactory.getLogger();

    /**
     * This is the currentUser that we will use for creating specific test data
     * If none specified we will use the admin user defined in
     * default.properties
     */
    private UserModel currentUser;

    /**
     * The current path where we want to create test data
     * It can be the root path, or /Shared or /Sites/<sitename> etc.
     */
    private String currentPath = "/";

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
    @SuppressWarnings("unchecked")
    public Data usingUser(UserModel user)
    {
        currentUser = user;
        return (Data) this;
    }

    @SuppressWarnings("unchecked")
    public Data usingAdmin()
    {
        currentUser = getAdminUser();
        return (Data) this;
    }

    protected UserModel getCurrentUser()
    {
        if (currentUser == null)
        {
            usingAdmin();
        }
        return currentUser;
    }

    @SuppressWarnings("unchecked")
    public Data usingPath(String path)
    {
        currentPath = path;
        return (Data) this;
    }

    protected String getCurrentPath()
    {
        return currentPath;
    }

    public UserModel getAdminUser()
    {
        return new UserModel(tasProperties.getAdminUser(), tasProperties.getAdminPassword());
    }

}
