package org.alfresco.utility.data;

import static org.alfresco.utility.report.log.Step.STEP;

import java.util.Arrays;
import java.util.List;

import org.alfresco.dataprep.SitePagesService;
import org.alfresco.utility.model.DiscussionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(value = "prototype")
public class DataDiscussion extends TestData<DataDiscussion>
{
    @Autowired SitePagesService sitePagesService;
    
    public DiscussionModel createRandomDiscussion()
    {
        String discussionTitle = RandomData.getRandomName("Discussion_");
        STEP(String.format("DATAPREP: Creating %s discussion with user %s in site %s", discussionTitle, getCurrentUser().getUsername(), getCurrentSite()));

        List<String> tags = Arrays.asList(RandomData.getRandomName("tag"), RandomData.getRandomName("tag"));
        String discussionText = RandomData.getRandomName("Discussion_");
        sitePagesService.createDiscussion(getCurrentUser().getUsername(), getCurrentUser().getPassword(), getCurrentSite(), discussionTitle, discussionText, tags);
        String nodeRef =  sitePagesService.getLinkNodeRef(getCurrentUser().getUsername(), getCurrentUser().getPassword(), getCurrentSite(), discussionTitle);
        return new DiscussionModel(discussionTitle, discussionText, tags, nodeRef);
    }
}
