package org.alfresco.utility.data.provider;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.alfresco.utility.model.FileModel;
import org.alfresco.utility.model.FolderModel;
import org.alfresco.utility.model.SiteModel;
import org.alfresco.utility.model.UserModel;

@XmlRootElement(name = "inputtestdata")
public class InputTestData
{
    protected List<UserModel> users;
    protected List<FolderModel> folders;
    protected List<FileModel> files;
    protected List<SiteModel> sites;

    @XmlElementWrapper
    @XmlElement(name = "user")
    public List<UserModel> getUsers()
    {
        if (users == null)
        {
            users = new ArrayList<UserModel>();
        }
        return users;
    }

    public void setUsers(List<UserModel> users)
    {
        this.users = users;
    }

    @XmlElementWrapper
    @XmlElement(name = "folder")
    public List<FolderModel> getFolders()
    {
        if (folders == null)
        {
            folders = new ArrayList<FolderModel>();
        }
        return folders;
    }

    public void setFolders(List<FolderModel> folders)
    {
        this.folders = folders;
    }

    @XmlElementWrapper
    @XmlElement(name = "file")
    public List<FileModel> getFiles()
    {
        if (files == null)
        {
            files = new ArrayList<FileModel>();
        }

        return files;
    }

    public void setFiles(List<FileModel> files)
    {
        this.files = files;
    }

    @XmlElementWrapper
    @XmlElement(name = "site")
    public List<SiteModel> getSites()
    {
        if (sites == null)
        {
            sites = new ArrayList<SiteModel>();
        }
        return sites;
    }

    public void setSites(List<SiteModel> sites)
    {
        this.sites = sites;
    }
}
