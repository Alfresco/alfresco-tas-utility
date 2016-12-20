package org.alfresco.utility.data;

import org.alfresco.dataprep.SitePagesService;
import org.alfresco.utility.model.WikiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static org.alfresco.utility.report.log.Step.STEP;

@Service
@Scope(value = "prototype")
public class DataWiki extends TestData<DataWiki>
{
    @Autowired
    SitePagesService sitePagesService;

    public WikiModel createRandomWiki()
    {
        String wikiTitle = RandomData.getRandomName("Wiki_");
        STEP(String.format("DATAPREP: Creating wiki %s with user %s in site %s", wikiTitle, getCurrentUser().getUsername(), getCurrentSite()));
        String wikiContent = RandomData.getRandomName("Wiki_");
        List<String> tags = Arrays.asList(RandomData.getRandomName("tag"), RandomData.getRandomName("tag"));
        sitePagesService.createWiki(getCurrentUser().getUsername(), getCurrentUser().getPassword(), getCurrentSite(), wikiTitle, wikiContent, tags);
        String nodeRef =  sitePagesService.getLinkNodeRef(getCurrentUser().getUsername(), getCurrentUser().getPassword(), getCurrentSite(), wikiTitle);
        return new WikiModel(wikiTitle, wikiContent, tags, nodeRef);
    }
}
