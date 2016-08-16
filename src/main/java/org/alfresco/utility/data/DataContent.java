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
        LOG.info("Asserting content exist in repository: {}", fullPath);
        boolean contentExist = !contentService.getNodeRefByPath(
                                                    tasProperties.getAdminUser(), 
                                                    tasProperties.getAdminPassword(), fullPath)
                                                .isEmpty();
        Assert.assertTrue(contentExist, String.format("Content {%s} was found in repository", fullPath));
    }
}
