package org.alfresco.utility.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Bogdan Bocancea
 */
public class CustomContentModel extends TestModel
{
    @JsonProperty(required = true)
    private String name;

    @JsonProperty(required = true)
    private String namespaceUri;

    @JsonProperty(required = true)
    private String namespacePrefix;

    private String status;
    private String author;

    public CustomContentModel()
    {
    }
    
    public CustomContentModel(String name)
    {
        setName(name);
    }
    
    public CustomContentModel(String name, String namespaceUri, String namespacePrefix)
    {
        setName(name);
        setNamespaceUri(namespaceUri);
        setNamespacePrefix(namespacePrefix);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getNamespaceUri()
    {
        return namespaceUri;
    }

    public void setNamespaceUri(String namespaceUri)
    {
        this.namespaceUri = namespaceUri;
    }

    public String getNamespacePrefix()
    {
        return namespacePrefix;
    }

    public void setNamespacePrefix(String namespacePrefix)
    {
        this.namespacePrefix = namespacePrefix;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }
}
