package org.alfresco.utility.data;

import org.alfresco.dataprep.CMISUtil.DocumentType;
import org.alfresco.dataprep.ContentAspects;
import org.alfresco.dataprep.ContentService;
import org.alfresco.utility.Utility;
import org.alfresco.utility.model.ContentModel;
import org.alfresco.utility.model.FileModel;
import org.alfresco.utility.model.FolderModel;
import org.alfresco.utility.model.SiteModel;
import org.alfresco.utility.model.UserModel;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testng.Assert;

/**
 * Utility for creating files and folders, asserting they exist in repository
 */
@Service
public class DataContent extends TestData<DataContent>
{
    @Autowired
    private ContentService contentService;

    @Autowired
    private ContentAspects contentAspect;

    public FolderModel createFolder(String folderName)
    {
        LOG.info("Creating a new folder content {} in {} ", folderName, getCurrentSpace());

        String location = Utility.buildPath(getCurrentSpace(), folderName);
        setLastResource(location);
        Folder cmisFolder = contentService.createFolderInRepository(getCurrentUser().getUsername(), getCurrentUser().getPassword(), folderName,
                getCurrentSpace());
        FolderModel folderModel = new FolderModel(cmisFolder.getPath());
        folderModel.setNodeRef(cmisFolder.getId());
        return folderModel;
    }

    /**
     * It will create a folder in current resource
     */
    public FolderModel createFolder()
    {
        FolderModel folderModel = new FolderModel(RandomData.getRandomName("Folder"));
        return createFolder(folderModel.getLocation());
    }

    public void addEmailAlias(SiteModel site, String folderName, String alias)
    {
        contentAspect.addEmailAlias(getCurrentUser().getUsername(), getCurrentUser().getPassword(), site.getId(), folderName, alias);
    }

    /**
     * Creates a random document based on {@link DocumentType} passed
     * Return the {@link Document} object on success creation
     * 
     * @param documentType
     * @return
     */
    public FileModel createContent(DocumentType documentType)
    {
        String newContent = String.format("%s.%s", RandomData.getRandomName("file"), Utility.cmisDocTypeToExtentions(documentType));
        String location = getLastResource();
        LOG.info("Creating a new non-empty content {} in {} ", newContent, location);

        if (getLastResource().isEmpty())
            setLastResource(RandomData.getRandomName("Folder"));

        Document cmisDocument = contentService.createDocumentInRepository(getCurrentUser().getUsername(), getCurrentUser().getPassword(), location,
                documentType, newContent, "This is a file file");
        FileModel newFile = new FileModel(cmisDocument.getPaths().get(0).toString());
        newFile.setNodeRef(cmisDocument.getId());
        return newFile;
    }

    /**
     * @param fullPath - the full path to CMIS object
     * @param userModel
     */
    public void assertContentExist(String fullPath)
    {
        LOG.info("Using User {}, asserting that content Exist in Repository: {}", getCurrentUser().toString(), fullPath);
        boolean contentExist = !checkContent(fullPath, getCurrentUser());
        Assert.assertTrue(contentExist, String.format("Content {%s} was found in repository", fullPath));
    }

    /**
     * @param fullPath - the full path to CMIS object
     * @param userModel
     */
    public void assertContentExist(ContentModel contentModel)
    {
        assertContentExist(contentModel.getLocation());
    }

    public void assertContentDoesNotExist(String fullPath)
    {
        LOG.info("Using User {}, asserting that content Does NOT Exist in Repository {}", getCurrentUser().toString(), fullPath);
        boolean contentDoesNotExist = !checkContent(fullPath, getCurrentUser());
        Assert.assertFalse(contentDoesNotExist, String.format("Content {%s} was NOT found in repository", fullPath));
    }

    private boolean checkContent(String fullPath, UserModel userModel)
    {
        return contentService.getNodeRefByPath(userModel.getUsername(), userModel.getPassword(), Utility.convertBackslashToSlash(fullPath)).isEmpty();
    }

    public void waitUntilContentIsDeleted(String fullPath)
    {
        int retry = 0;
        String deletedObject = contentService.getNodeRefByPath(getCurrentUser().getUsername(), getCurrentUser().getPassword(), fullPath);
        while (!StringUtils.isEmpty(deletedObject) && retry < Utility.retryCountSeconds)
        {
            Utility.waitToLoopTime(1);
            deletedObject = contentService.getNodeRefByPath(getCurrentUser().getUsername(), getCurrentUser().getPassword(), fullPath);
            retry++;
        }
    }
}