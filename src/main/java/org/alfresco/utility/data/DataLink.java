package org.alfresco.utility.data;

import org.alfresco.dataprep.SitePagesService;
import org.alfresco.utility.model.LinkModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static org.alfresco.utility.report.log.Step.STEP;

/**
 * Created by Claudia Agache on 11/18/2016.
 */
@Service
@Scope(value = "prototype")
public class DataLink extends TestData<DataLink>
{
    @Autowired SitePagesService sitePagesService;

    public LinkModel createRandomLink()
    {
        String linkTitle = RandomData.getRandomName("Link");
        String linkUrl = "www.google.com";
        STEP(String.format("DATAPREP: Creating link %s with user %s in site %s", linkTitle, getCurrentUser().getUsername(), getCurrentSite()));
        sitePagesService.createLink(getCurrentUser().getUsername(), getCurrentUser().getPassword(), getCurrentSite(), linkTitle, linkUrl, "", false, null);
        String nodeRef =  sitePagesService.getLinkNodeRef(getCurrentUser().getUsername(), getCurrentUser().getPassword(), getCurrentSite(), linkTitle);
        return new LinkModel(linkTitle, linkUrl, "", false, nodeRef);
    }
}
