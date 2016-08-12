package org.alfresco.tester.model;

import java.io.File;

public class DocumentModel extends TestModel
{ 
    private String cmisObjecTypeId = "cmis:document";
    private String title;
    private String description;
    private String name;
    private File path;
    private String content;

    private DocumentType documentType;
    
    public DocumentModel()
    {
    }
    
    public DocumentModel(String name, String title, String description, String content, DocumentType documentType, File path)
    {
        setName(name);
        setTitle(title);
        setDescription(description);
        setContent(content);
        setDocumentType(documentType);
        setPath(path);
    }
    
    public String getCmisObjecTypeId()
    {
        return cmisObjecTypeId;
    }
    
    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public File getPath()
    {
        return path;
    }

    public void setPath(File path)
    {
        this.path = path;
    }

    public DocumentType getDocumentType()
    {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType)
    {
        this.documentType = documentType;
    }
}
