package org.alfresco.utility.data;

import org.alfresco.dataprep.ContentAspects;
import org.alfresco.dataprep.ContentService;
import org.alfresco.utility.Utility;
import org.alfresco.utility.model.SiteModel;
import org.alfresco.utility.model.UserModel;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testng.Assert;

@Service
public class DataContent extends TestData<DataContent>
{
    @Autowired
    private ContentService contentService;
    
    @Autowired
    private ContentAspects contentAspect;
    
    public Folder createFolder(String folderName, SiteModel site)
    {
        return contentService.createFolder(tasProperties.getAdminUser(), 
                    tasProperties.getAdminPassword(), folderName, site.getId());
    }

    public void addEmailAlias(SiteModel site, String folderName, String alias) {
        contentAspect.addEmailAlias(tasProperties.getAdminUser(), tasProperties.getAdminPassword(), 
                site.getId(), folderName, alias);
    }
    
    /**     
     * @param fullPath - the full path to CMIS object
     * @param userModel
     */
    public void assertContentExist(String fullPath, UserModel userModel)
    {   
        LOG.info("Using User {}, asserting that content Exist in Repository {}",userModel.toString(), fullPath);        
        boolean contentExist = !checkContent(fullPath, userModel);
        Assert.assertTrue(contentExist, String.format("Content {%s} was found in repository", fullPath));
    }
    
    public void assertContentDoesNotExist(String fullPath, UserModel userModel)
    {        
        LOG.info("Using User {}, asserting that content Does NOT Exist in Repository {}",userModel.toString(), fullPath);
        boolean contentDoesNotExist = !checkContent(fullPath, userModel);
        Assert.assertFalse(contentDoesNotExist, String.format("Content {%s} was NOT found in repository", fullPath));
    }
        
    private boolean checkContent(String fullPath, UserModel userModel)
    {
        return contentService.getNodeRefByPath(userModel.getUsername(), userModel.getPassword(), 
                Utility.convertBackslashToSlash(fullPath)).isEmpty();
    }
    
}