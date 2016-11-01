package org.alfresco.utility.data.provider;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.alfresco.utility.LogFactory;
import org.alfresco.utility.constants.UserRole;
import org.alfresco.utility.data.DataContent;
import org.alfresco.utility.data.DataSite;
import org.alfresco.utility.data.DataUser;
import org.alfresco.utility.exception.DataPreparationException;
import org.alfresco.utility.model.FileModel;
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
public class XMLTestData extends XMLCollection
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
        logEntireStructure();
    }

    /**
     * Calling this method will create entire test data structure in your alfresco repository
     * 
     * @throws Exception
     */
    public void createSitesStructure(DataSite dataSite, DataContent dataContent, DataUser dataUser) throws Exception
    {
        for (XMLSiteData site : getSites())
        {
            if (dataSite.usingAdmin().isSiteCreated(site.getModel()))
            {
                LOG.info("Skipping Site: {}. This site already exists in repository!", site.getFullLocation());
            }
            else
            // create site with the user provided in XML file
            {
                /*
                 * get the user model of the site
                 */
                LOG.info("Creating Site: {}", site.getFullLocation());

                UserModel user = getUserBy(dataContent.getAdminUser(), site.getCreatedBy());
                dataSite.usingUser(user).createSite(site.getModel());
            }
            addMembers(site.getMembers(), site.getModel(), dataUser);
            createFilesStructure(site.getFiles(), site.getModel(), dataContent);
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

            FolderModel folderInRepo = null;

            /*
             * create a custom folder model
             */
            if (folder.isCustomModel())
            {
                LOG.info("Creating Custom Folder: {}", folder.getModel().toString());
                folderInRepo = (FolderModel) dataContent.usingUser(userFolder).setCurrentSpace(location).createCustomContent(folder.getModel(),
                        folder.getCustomModel().getName(), folder.getCustomModel().getObjectTypeProperties());
            }
            else // create a basic folder model using standard CMIS object type id
            {

                folderInRepo = dataContent.usingUser(userFolder).setCurrentSpace(location).createFolder(folder.getModel());
            }

            addComments(folderInRepo.getCmisLocation(), folder.getComments(), dataContent);
            addTags(folderInRepo.getCmisLocation(), folder.getTags(), dataContent);

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
    private void createFilesStructure(List<XMLFileData> filesStructure, TestModel parentFolder, DataContent dataContent) throws Exception
    {
        // create files
        for (XMLFileData file : filesStructure)
        {
            /*
             * get the user model of the folder
             */
            UserModel userFile = getUserBy(dataContent.getAdminUser(), file.getCreatedBy());
            FileModel contentInRepo = null;
            if (parentFolder instanceof FolderModel)
            {
                FolderModel parentFoldeInCmis = (FolderModel) parentFolder;
                if (file.isCustomModel())
                {
                    dataContent.usingUser(userFile).usingResource(parentFoldeInCmis).setCurrentSpace(parentFoldeInCmis.getCmisLocation());
                    contentInRepo = (FileModel) dataContent.createCustomContent(file.getModel(), file.getCustomModel().getName(),
                            file.getCustomModel().getObjectTypeProperties());
                }
                else
                {
                    dataContent.usingUser(userFile).usingResource(parentFoldeInCmis).setCurrentSpace(parentFoldeInCmis.getCmisLocation());
                    contentInRepo = dataContent.usingUser(userFile).createContent(file.getModel());
                }
            }

            if (parentFolder instanceof SiteModel)
            {
                if (file.isCustomModel())
                    contentInRepo = (FileModel) dataContent.usingUser(userFile).usingSite((SiteModel) parentFolder).createCustomContent(file.getModel(),
                            file.getCustomModel().getName(), file.getCustomModel().getObjectTypeProperties());
                else
                    contentInRepo = dataContent.usingUser(userFile).usingSite((SiteModel) parentFolder).createContent(file.getModel());
            }
            addComments(contentInRepo.getCmisLocation(), file.getComments(), dataContent);
            addTags(contentInRepo.getCmisLocation(), file.getTags(), dataContent);
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
                dataContent.usingAdmin().deleteTree(folder.getModel());
            }
            for (XMLFileData file : site.getFiles())
            {
                if (dataContent.checkContent(file.getModel().getCmisLocation(),  dataContent.getAdminUser()))
                    try
                    {
                        dataContent.usingAdmin().usingResource(file.getModel()).deleteContent();
                    }
                    catch (Exception e)
                    {
                        LOG.error("Could not delete file: {}", file.getModel().toString());
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
                user = u.getModel();
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

    /**
     * Add users as members with role to site
     * 
     * @param membersStructure a list of users to be added as members
     * @param inSite
     * @param dataUser
     * @throws Exception
     */
    private void addMembers(List<XMLUserData> membersStructure, SiteModel siteModel, DataUser dataUser) throws Exception
    {
        // add members to site
        for (XMLUserData user : membersStructure)
        {
            // get UserModel from XML structure
            UserModel userFile = getUserBy(dataUser.getAdminUser(), user.getName());

            dataUser.addUserToSite(userFile, siteModel, UserRole.valueOf(user.getRole()));
        }
    }

    /*
     * apply all comments to created file
     */
    private void addComments(String objectPathInCmis, List<XMLCommentData> comments, DataContent dataContent) throws DataPreparationException
    {
        if (comments.size() > 0)
            LOG.info("Adding Comments Count: {} to object: {}", comments.size(), objectPathInCmis);

        for (XMLCommentData comment : comments)
        {
            UserModel userComment = getUserBy(dataContent.getAdminUser(), comment.getCreatedBy());
            dataContent.getContentActions().addComment(userComment.getUsername(), userComment.getPassword(), objectPathInCmis, comment.getValue());
        }
    }

    /*
     * applying all tags to created file
     */
    private void addTags(String objectPathInCmis, List<XMLTagData> tags, DataContent dataContent) throws DataPreparationException
    {
        if (tags.size() > 0)
            LOG.info("Adding Tags Count: {} to object: {}", tags.size(), objectPathInCmis);
        for (XMLTagData tag : tags)
        {
            UserModel userTag = getUserBy(dataContent.getAdminUser(), tag.getCreatedBy());
            dataContent.usingUser(userTag).addTagToContent(objectPathInCmis, tag.getModel());
        }
    }

    @Override
    protected List<XMLDataItem> getImbricatedData()
    {
        this.entireStructure.addAll(getUsers());
        for (XMLSiteData site : getSites())
        {
            this.entireStructure.addAll(site.getEntireStructure());
        }
        return entireStructure;
    }

    public void logEntireStructure()
    {
        LOG.info("Summarizing the Test Data Structure:");
        for (XMLDataItem item : getEntireStructure())
        {
            LOG.info("Found Test Data Model [{}] with -> id [{}], info: {}", item.getClass(), item.getId(), item.toString());
        }
    }

    /**
     * @param id
     * @return {@link XMLDataItem} based on the id of the object
     */
    public XMLDataItem getTestDataItemWithId(String id)
    {
        LOG.info("Searching for Test Data Item with id: {}", id);
        XMLDataItem dataFound = null;
        for (XMLDataItem item : getEntireStructure())
        {
            if (item.getId() == null)
            {
                LOG.error("Test Data Item  [{}] does not have id assigned", item.toString());
                continue;
            }
            if (item.getId().equals(id))
            {
                dataFound = item;
                break;
            }
        }
        return dataFound;
    }
}
