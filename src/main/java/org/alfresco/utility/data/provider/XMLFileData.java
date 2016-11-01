package org.alfresco.utility.data.provider;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

import org.alfresco.utility.model.FileModel;
import org.alfresco.utility.model.FileType;

/**
 * <file name="a.txt" createdBy="paul">
 * <content>abce</content>
 * </file>
 * 
 * @author Paul Brodner
 */
@XmlType(name = "file")
public class XMLFileData extends XMLCollection implements XMLDataItem
{
    private String name;
    private String createdBy;
    private String content;
    private String id;

    private XMLCustomModel customModel;
    private List<XMLCommentData> comments = new ArrayList<XMLCommentData>();
    private List<XMLTagData> tags = new ArrayList<XMLTagData>();
    private List<XMLAspectData> aspects = new ArrayList<XMLAspectData>();

    private FileModel model = new FileModel();

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

    @XmlElement(name = "content")
    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    @Override
    public FileModel getModel()
    {
        model.setName(getName());
        model.setFileType(FileType.fromName(getName()));
        model.setContent(getContent());
        model.setCmisLocation(String.format("%s/%s", getParent(), getName()));
        return model;
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
        return entireStructure;
    }

    @Override
    public String toString()
    {
        StringBuilder info = new StringBuilder();
        info.append("file[name='").append(getName()).append("',").append("createdBy='").append(getCreatedBy()).append("', id='").append(getId()).append("']");

        return info.toString();
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
