package org.alfresco.utility.data;

import org.alfresco.dataprep.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testng.Assert;

@Service
public class DataContent extends TestData
{
    @Autowired
    private ContentService contentService;

    public void assertContentExist(String fullPath)
    {        
        boolean contentExist = !checkContent(fullPath);
        Assert.assertTrue(contentExist, String.format("Content {%s} was found in repository", fullPath));
    }
    
    public void assertContentDoesNotExist(String fullPath)
    {        
        boolean contentDoesNotExist = checkContent(fullPath);
        Assert.assertTrue(contentDoesNotExist, String.format("Content {%s} was NOT found in repository", fullPath));
    }
    
    
    private boolean checkContent(String fullPath)
    {
        LOG.info("Check for content in repository: {}", fullPath);
        return contentService.getNodeRefByPath(tasProperties.getAdminUser(), 
                                               tasProperties.getAdminPassword(), fullPath)
                                                .isEmpty();
    }
}
