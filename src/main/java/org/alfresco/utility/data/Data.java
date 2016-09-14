package org.alfresco.utility.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.alfresco.utility.model.FileModel;
import org.alfresco.utility.model.FolderModel;
import org.alfresco.utility.model.SiteModel;
import org.alfresco.utility.model.UserModel;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Data
{

    private Folders folders;

    private Files files;

    private Sites sites;
    
    private Users users;

    public Users getUsers()
    {
        return users;
    }

    public void setUsers(Users users)
    {
        this.users = users;
    }

    public Sites getSites()
    {
        return sites;
    }

    public void setSites(Sites sites)
    {
        this.sites = sites;
    }

    public Folders getFolders()
    {
        return folders;
    }

    public void setFolders(Folders folders)
    {
        this.folders = folders;
    }

    public Files getFiles()
    {
        return files;
    }

    public void setFiles(Files files)
    {
        this.files = files;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "folder" })
    public static class Folders
    {

        protected List<FolderModel> folder;

        public List<FolderModel> getFolder()
        {

            if (folder == null)
            {
                folder = new ArrayList<FolderModel>();
            }

            return folder;
        }

        public void setFolder(List<FolderModel> folder)
        {
            this.folder = folder;
        }

    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "file" })
    public static class Files
    {

        protected List<FileModel> file;

        public List<FileModel> getFile()
        {
            if (file == null)
            {
                file = new ArrayList<FileModel>();
            }

            return file;
        }

        public void setFile(List<FileModel> file)
        {
            this.file = file;
        }

    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "site" })
    public static class Sites
    {

        protected List<SiteModel> site;

        public List<SiteModel> getSite()
        {
            if (site == null)
            {
                site = new ArrayList<SiteModel>();
            }

            return site;
        }

        public void setSite(List<SiteModel> site)
        {
            this.site = site;
        }

    }
    
    
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "user" })
    public static class Users
    {

        protected List<UserModel> user;

        public List<UserModel> getUser()
        {

            if (user == null)
            {
                user = new ArrayList<UserModel>();
            }

            return user;
        }

        public void setUser(List<UserModel> user)
        {
            this.user = user;
        }

    }


}
