package org.alfresco.utility.model;


import java.util.List;

public class BlogPostModel extends TestModel
{
    private String title;
    private String content;
    private boolean draft;
    private List<String> tags;

    public BlogPostModel(String title, String content, boolean draft, List<String> tags)
    {
        this.title = title;
        this.content = content;
        this.draft = draft;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isDraft() {
        return draft;
    }

    public void setDraft(boolean draft) {
        this.draft = draft;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
