package org.alfresco.utility.data.provider;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.alfresco.utility.LogFactory;
import org.alfresco.utility.data.DataContent;
import org.alfresco.utility.data.DataSite;
import org.alfresco.utility.data.DataUser;
import org.alfresco.utility.exception.DataPreparationException;
import org.alfresco.utility.model.FolderModel;
import org.alfresco.utility.model.QueryModel;
import org.alfresco.utility.model.SiteModel;
import org.alfresco.utility.model.TestModel;
import org.alfresco.utility.model.UserModel;
import org.slf4j.Logger;

/**
 * XML structure for Test Data
 * 
 * @author Paul Brodner
 */
@XmlRootElement(name = "testData")
public class XMLTestData
{
    static Logger LOG = LogFactory.getLogger();

    private List<QueryModel> queries;
    private List<XMLSiteData> sites = new ArrayList<XMLSiteData>();
    private List<XMLUserData> users;

    @XmlElementWrapper
    @XmlElement(name = "query")
    public List<QueryModel> getQueries()
    {
        return queries;
    }

    public void setQueries(List<QueryModel> queries)
    {
        this.queries = queries;
    }

    @XmlElementWrapper
    @XmlElement(name = "site")
    public List<XMLSiteData> getSites()
    {
        return sites;
    }

    public void setSites(List<XMLSiteData> sites)
    {
        this.sites = sites;
    }

    @XmlElementWrapper
    @XmlElement(name = "user")
    public List<XMLUserData> getUsers()
    {
        return users;
    }

    public void setUsers(List<XMLUserData> users)
    {
        this.users = users;
    }

    /**
     * Calling this method will create entire test data structure in your alfresco repository
     * 
     * @throws DataPreparationException
     */
    public void createUsers(DataUser dataUser) throws DataPreparationException
    {
        for (XMLUserData user : getUsers())
        {
            if (dataUser.isUserInRepo(user.getName()))
            {
                LOG.info("Skipping USER data:{}. This user already exists in repository!", user.toString());
            }
            else
            {
                LOG.info("Creating USER data:" + user.toString());
                dataUser.createUser(user.getName(), user.getPassword());
            }
        }
    }

    /**
     * Calling this method will create entire test data structure in your alfresco repository
     * 
     * @throws Exception
     */
    public void createSitesStructure(DataSite dataSite, DataContent dataContent) throws Exception
    {
        for (XMLSiteData site : getSites())
        {
            if (dataSite.usingAdmin().isSiteCreated(site.toModel()))
            {
                LOG.info("Skipping Site: {}. This site already exists in repository!", site.getFullLocation());
            }
            else // create site with the user provided in XML file
            {
                /*
                 * get the user model of the site
                 */
                UserModel user = getUserBy(dataContent.getAdminUser(), site.getCreatedBy());
                dataSite.usingUser(user).createSite(site.toModel());
            }

            createFilesStructure(site.getFiles(), site.toModel(), dataContent);
            createFolderStructure(site.getFolders(), site.getFullLocation(), dataContent);
        }
    }

    /**
     * Creates folder structure
     * 
     * @param folderStructure
     * @param inSite
     * @param dataContent
     * @throws Exception
     */
    private void createFolderStructure(List<XMLFolderData> folderStructure, String location, DataContent dataContent) throws Exception
    {
        // create structure in site
        for (XMLFolderData folder : folderStructure)
        {
            /*
             * get the user model of the folder
             */
            UserModel userFolder = getUserBy(dataContent.getAdminUser(), folder.getCreatedBy());

            FolderModel folderInRepo = dataContent.usingUser(userFolder).setCurrentSpace(location).createFolder(folder.toModel());
            createFilesStructure(folder.getFiles(), folderInRepo, dataContent);
            createFolderStructure(folder.getFolders(), folderInRepo.getCmisLocation(), dataContent);
        }
    }

    /**
     * Creates file structure
     * 
     * @param filesStructure
     * @param inFolder
     * @param dataContent
     * @throws Exception
     */
    private void createFilesStructure(List<XMLFileData> filesStructure, TestModel testModel, DataContent dataContent) throws Exception
    {
        // create files
        for (XMLFileData file : filesStructure)
        {
            /*
             * get the user model of the folder
             */
            UserModel userFile = getUserBy(dataContent.getAdminUser(), file.getCreatedBy());
            if (testModel instanceof FolderModel)
            {
                dataContent.usingUser(userFile).usingResource((FolderModel) testModel).createContent(file.toModel());
            }

            if (testModel instanceof SiteModel)
            {
                dataContent.usingUser(userFile).usingSite((SiteModel) testModel).createContent(file.toModel());
            }
        }
    }

    /**
     * Calling this method will delete entire test data structure from your alfresco repository
     * 
     * @throws Exception
     */
    public void cleanup(DataContent dataContent)
    {
        LOG.info("Startig CLEANUP process...");
        for (XMLSiteData site : getSites())
        {
            for (XMLFolderData folder : site.getFolders())
            {
                dataContent.usingAdmin().deleteTree(folder.toModel());
            }

            for (XMLFileData file : site.getFiles())
            {
                if (dataContent.checkContent(file.toModel().getCmisLocation(), dataContent.getAdminUser()))
                    try
                    {
                        dataContent.usingAdmin().usingResource(file.toModel()).deleteContent();
                    }
                    catch (Exception e)
                    {
                        LOG.error("Could not delete file: {}", file.toModel().toString());
                    }
            }
        }
        LOG.info("CLEANUP finished!");
    }

    /**
     * @param admin
     * @param username
     * @return UserModel based on the username provider, or the Admin UserModel if "admin" is used in XML input file
     * @throws DataPreparationException
     */
    private UserModel getUserBy(UserModel admin, String username) throws DataPreparationException
    {
        if (username.toLowerCase().equals("admin"))
            return admin;
        /*
         * get the user model of the site
         */
        UserModel user = null;
        for (XMLUserData u : users)
        {
            if (u.getName().equals(username))
            {
                user = u.toModel();
                break;
            }
        }

        if (user == null)
        {
            String info = String.format("You want to use user [%s] but this wasn't specified in <users> section in your xml file.", username);
            throw new DataPreparationException(info);
        }

        return user;
    }

    @Override
    public String toString()
    {
        StringBuilder info = new StringBuilder();
        info.append("xmlFileInputData-PREPARING[users=").append(getUsers().size()).append(", sites=").append(getSites().size()).append("]");
        return info.toString();
    }
}
