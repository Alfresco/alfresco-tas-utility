package org.alfresco.utility.data.provider;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

import org.alfresco.utility.model.FileModel;

/**
 * <file name="a.txt" createdBy="paul">
 * <content>abce</content>
 * </file>
 * 
 * @author Paul Brodner
 */
@XmlType(name = "file")
public class XMLFileData implements XMLDataItem
{
    private String parent;
    
    private String name;
    private String createdBy;
    private String content;

    private List<XMLTagData> tags = new ArrayList<XMLTagData>();
    
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
        FileModel f = new FileModel(getName());
        f.setContent(getContent());
        f.setCmisLocation(String.format("%s/%s", getParent(), getName()));
        return f;
    }

    public String getParent()
    {
        return parent;
    }

    public void setParent(String parent)
    {
        this.parent = parent;
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

}
