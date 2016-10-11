package org.alfresco.utility.data;

import static org.alfresco.utility.report.log.Step.STEP;

import org.alfresco.dataprep.CMISUtil.DocumentType;
import org.alfresco.dataprep.ContentAspects;
import org.alfresco.dataprep.ContentService;
import org.alfresco.utility.Utility;
import org.alfresco.utility.exception.DataPreparationException;
import org.alfresco.utility.model.ContentModel;
import org.alfresco.utility.model.FileModel;
import org.alfresco.utility.model.FolderModel;
import org.alfresco.utility.model.UserModel;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.commons.exceptions.CmisStorageException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.testng.Assert;

/**
 * Utility for creating files and folders, asserting they exist in repository
 */
@Service
@Scope(value = "prototype")
public class DataContent extends TestData<DataContent>
{
    @Autowired
    private ContentService contentService;

    @Autowired
    private ContentAspects contentAspect;

    /**
     * It will create a new folder in current resource
     */
    public FolderModel createFolder(FolderModel folderModel)
    {
        STEP(String.format("DATAPREP: Creating a new folder content %s in %s ", folderModel.getName(), getCurrentSpace()));
        String location = Utility.buildPath(getCurrentSpace(), folderModel.getName());
        setLastResource(location);
        Folder cmisFolder = contentService.createFolderInRepository(getCurrentUser().getUsername(), getCurrentUser().getPassword(), folderModel.getName(),
                getCurrentSpace());
        folderModel.setProtocolLocation(location);
        folderModel.setNodeRef(cmisFolder.getId());
        folderModel.setCmisLocation(location);
        return folderModel;
    }

    /**
     * It will create a random folder in current resource
     */
    public FolderModel createFolder()
    {
        String folderName = RandomData.getRandomName("Folder");
        STEP(String.format("DATAPREP: Create folder '%s' in %s", folderName, getCurrentSpace()));
        FolderModel folderModel = new FolderModel(folderName);

        String location = Utility.buildPath(getCurrentSpace(), folderName);
        setLastResource(location);
        Folder cmisFolder = contentService.createFolderInRepository(getCurrentUser().getUsername(), getCurrentUser().getPassword(), folderName,
                getCurrentSpace());
        folderModel.setProtocolLocation(cmisFolder.getPath());
        folderModel.setCmisLocation(cmisFolder.getPath());
        folderModel.setNodeRef(cmisFolder.getId());
        return folderModel;
    }

    /**
     * Add Email Alias aspect (emailserver:aliasable)
     * 
     * @param alias
     * @return
     * @throws Exception
     */
    public String addEmailAlias(String alias) throws Exception
    {        
        Utility.checkObjectIsInitialized(getLastResource(), "getLastResource()");
        STEP(String.format("DATAPREP: Add 'Email Alias' aspect to folder '%s'", getLastResource()));
        contentAspect.addEmailAlias(getCurrentUser().getUsername(), getCurrentUser().getPassword(), getCurrentSite(), getLastResource(), alias);
        return alias;
    }

    /**
     * Creates a random document based on {@link DocumentType} passed
     * Return the {@link Document} object on success creation
     * 
     * @param documentType
     * @return
     * @throws DataPreparationException 
     */
    public FileModel createContent(DocumentType documentType) throws DataPreparationException
    {
        String newContent = String.format("%s.%s", RandomData.getRandomName("file"), Utility.cmisDocTypeToExtentions(documentType));
        String newLocation = Utility.buildPath(getLastResource(), newContent);
        STEP(String.format("DATAPREP: Creating a new non-empty content %s in %s ", newContent, getLastResource()));

        if (getLastResource().isEmpty())
            setLastResource(RandomData.getRandomName("Folder"));

        Document cmisDocument = null; 
        
        try
        {
            cmisDocument = contentService.createDocumentInRepository(getCurrentUser().getUsername(), getCurrentUser().getPassword(), getLastResource(),
                    documentType, newContent, "This is a file file");
        }
        catch (CmisStorageException cse)
        {
            LOG.error(cse.getMessage());
            throw new DataPreparationException(cse.getMessage());
        }
        
        FileModel newFile = new FileModel(cmisDocument.getName());
        newFile.setCmisLocation(newLocation);
        newFile.setProtocolLocation(newLocation);
        newFile.setNodeRef(cmisDocument.getId());
        return newFile;
    }
    
    /**
     * Creates a random document based on {@link DocumentType} passed
     * Return the {@link Document} object on success creation
     * 
     * @param documentType
     * @param siteName
     * @return
     */
    public FileModel createContent(DocumentType documentType, String siteName)
    {
        String newContent = String.format("%s.%s", RandomData.getRandomName("file"), Utility.cmisDocTypeToExtentions(documentType));
        STEP(String.format("DATAPREP: Creating a new non-empty content %s in %s site", newContent, siteName));

        Document cmisDocument = contentService.createDocument(getCurrentUser().getUsername(), getCurrentUser().getPassword(), siteName,
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
        boolean contentExist = !checkContent(fullPath, getCurrentUser());
        Assert.assertTrue(contentExist, String.format("Content {%s} was found in repository", fullPath));
    }

    /**
     * @param fullPath - the full path to CMIS object
     * @param userModel
     */
    public void assertContentExist(ContentModel contentModel)
    {
        assertContentExist(contentModel.getCmisLocation());
    }

    public void assertContentDoesNotExist(String fullPath)
    {
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