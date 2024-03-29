package org.alfresco.utility.data.provider;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

import org.alfresco.dataprep.SiteService.Visibility;
import org.alfresco.utility.model.SiteModel;

/**
 * <site name="site1" createdBy="admin">
 */
@XmlType(name = "site")
public class XMLSiteData extends XMLCollection implements XMLDataItem 
{
    private String name;
    private String createdBy;
    private String visibility;
    private String id;

    private List<XMLFolderData> folders = new ArrayList<XMLFolderData>();
    private List<XMLFileData> files = new ArrayList<XMLFileData>();
    private List<XMLUserData> members = new ArrayList<XMLUserData>();
    private SiteModel model = new SiteModel();    

    @XmlAttribute(name = "visibility")
    public String getVisibility()
    {
        return visibility;
    }

    public void setVisibility(String visibility)
    {
        this.visibility = visibility;
    }

    @XmlAttribute(name = "name")
    public String getName()
    {
        return name;
    }

    public String getFullLocation()
    {
        return String.format("/Sites/%s/documentLibrary", getName());
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
    @XmlElement(name = "folder")
    public List<XMLFolderData> getFolders()
    {
        for (XMLFolderData f : folders)
        {
            f.setParent(getFullLocation());
        }
        return folders;
    }

    public void setFolders(List<XMLFolderData> folders)
    {
        this.folders = folders;
    }

    @Override
    public String toString()
    {
        StringBuilder info = new StringBuilder();
        info.append("site[name='").append(getName()).append("',")
            .append("createdBy='")
            .append(getCreatedBy()).append("']");

        return info.toString();
    }

    @Override
    public SiteModel getModel()
    {
        model.setId(getName());
        model.setTitle(getName());
        Visibility v = Visibility.PUBLIC;
        switch (getVisibility())
        {
            case "public":
                v = Visibility.PUBLIC;
                break;

            case "private":
                v = Visibility.PRIVATE;
                break;
        }

        model.setVisibility(v);
        return model;
    }

    @XmlElementWrapper
    @XmlElement(name = "file")
    public List<XMLFileData> getFiles()
    {
        for (XMLFileData f : files)
        {
            f.setParent(getFullLocation());
        }
        return files;
    }

    public void setFiles(List<XMLFileData> files)
    {
        for (XMLFileData f : files)
        {
            f.setParent(getFullLocation());
        }
        this.files = files;
    }

    @XmlElementWrapper
    @XmlElement(name = "user")
    public List<XMLUserData> getMembers()
    {
        return members;
    }

    public void setMembers(List<XMLUserData> members)
    {
        this.members = members;
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
        this.entireStructure.addAll(getMembers());
        for(XMLFileData file : getFiles())
        {
            file.setParent(getFullLocation());
            this.entireStructure.addAll(file.getEntireStructure());
        }
        for(XMLFolderData folder : getFolders())
        {
            folder.setParent(getFullLocation());
            this.entireStructure.addAll(folder.getEntireStructure());
        }
        return entireStructure;
    }

}
