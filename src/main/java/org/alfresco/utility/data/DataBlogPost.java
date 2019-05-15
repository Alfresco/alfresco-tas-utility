package org.alfresco.utility.data;


import org.alfresco.dataprep.SitePagesService;
import org.alfresco.utility.model.BlogPostModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static org.alfresco.utility.report.log.Step.STEP;

@Service
@Scope(value = "prototype")
public class DataBlogPost extends TestData<DataBlogPost>
{
    @Autowired
    SitePagesService sitePagesService;

    public BlogPostModel createRandomBlogPost()
    {
        String blogPost = RandomData.getRandomName("BlogPost");
        String blogPostBody = "blog post body";
        STEP(String.format("DATAPREP: Creating blog post %s with user %s in site %s", blogPost, getCurrentUser().getUsername(), getCurrentSite()));
        sitePagesService.createBlogPost(getCurrentUser().getUsername(), getCurrentUser().getPassword(), getCurrentSite(), blogPost, blogPostBody, false, null);
        return new BlogPostModel(blogPost, blogPostBody, false, null);
    }
}
