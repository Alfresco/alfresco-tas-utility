package org.alfresco.utility.model;

import java.util.List;

public class WikiModel extends TestModel
{
    private String title;
    private String content;
    private List<String> tags;
    private String nodeRef;

    public WikiModel(String title, String content, List<String> tags, String nodeRef)
    {
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.nodeRef = nodeRef;
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getNodeRef() {
        return nodeRef;
    }

    public void setNodeRef(String nodeRef) {
        this.nodeRef = nodeRef;
    }
}
