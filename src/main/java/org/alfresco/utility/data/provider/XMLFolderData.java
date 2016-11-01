package org.alfresco.utility.data.provider;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

import org.alfresco.utility.model.FolderModel;

/**
 * <folder name="f1" createdBy="user1">
 */
@XmlType(name = "folder")
public class XMLFolderData extends XMLCollection implements XMLDataItem
{
    private String name;
    private String createdBy;
    private String id;
    private List<XMLFileData> files = new ArrayList<XMLFileData>();
    private List<XMLFolderData> folders = new ArrayList<XMLFolderData>();
    private List<XMLCommentData> comments = new ArrayList<XMLCommentData>();
    private List<XMLTagData> tags = new ArrayList<XMLTagData>();
    private List<XMLAspectData> aspects = new ArrayList<XMLAspectData>();
    
    private XMLCustomModel customModel;

    private FolderModel model = new FolderModel();

    @XmlAttribute(name = "name")
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    
    @XmlAttribute(name = "createdBy")
    public String getCreatedBy()
    {
        return createdBy; 
    }

    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }

    @XmlElementWrapper
    @XmlElement(name = "file")
    public List<XMLFileData> getFiles()
    {
        return files;
    }

    public void setFiles(List<XMLFileData> files)
    {
        this.files = files;
    }

    @Override
    public String toString()
    {
        StringBuilder info = new StringBuilder();
        info.append("folder[name='").append(getName()).append("',").append("createdBy='").append(getCreatedBy()).append("', id='").append(getId()).append("']");
        return info.toString();
    }

    @Override
    public FolderModel getModel()
    {
        model.setName(getName());
        model.setCmisLocation(String.format("%s/%s", getParent(), getName()));
        return model;
    }

    @XmlElementWrapper
    @XmlElement(name = "folder")
    public List<XMLFolderData> getFolders()
    {
        for (XMLFolderData f : folders)
        {
            f.setParent(getModel().getCmisLocation());
        }
        return folders;
    }

    public void setFolders(List<XMLFolderData> folders)
    {
        this.folders = folders;
    }

    @XmlElementWrapper
    @XmlElement(name = "comment")
    public List<XMLCommentData> getComments()
    {
        return comments;
    }

    public void setComments(List<XMLCommentData> comments)
    {
        this.comments = comments;
    }

    @XmlElement(name = "custom-model")
    public XMLCustomModel getCustomModel()
    {
        return customModel;
    }

    public void setCustomModel(XMLCustomModel customModel)
    {
        this.customModel = customModel;
    }

    public boolean isCustomModel()
    {
        return customModel != null;
    }

    @XmlElementWrapper
    @XmlElement(name = "tag")
    public List<XMLTagData> getTags()
    {
        return tags;
    }

    public void setTags(List<XMLTagData> tags)
    {
        this.tags = tags;
    }

    @Override
    @XmlAttribute(name = "id")
    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    @Override
    protected List<XMLDataItem> getImbricatedData()
    {
        this.entireStructure.add(this);
        this.entireStructure.addAll(getComments());
        this.entireStructure.addAll(getTags());
        for (XMLFileData file : getFiles())
        {
            file.setParent(getModel().getCmisLocation());
            this.entireStructure.addAll(file.getEntireStructure());
        }
        for (XMLFolderData folder : getFolders())
        {
            folder.setParent(getModel().getCmisLocation());
            this.entireStructure.addAll(folder.getEntireStructure());
        }
        return entireStructure;
    }
    
    @XmlElementWrapper
    @XmlElement(name = "aspect")
    public List<XMLAspectData> getAspects()
    {
        return aspects;
    }

    public void setAspects(List<XMLAspectData> aspects)
    {
        this.aspects = aspects;
    }

}
