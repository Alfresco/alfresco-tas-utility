package org.alfresco.utility.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.alfresco.utility.model.FileModel;
import org.alfresco.utility.model.FolderModel;
import org.alfresco.utility.model.SiteModel;
import org.alfresco.utility.model.TestModel;
import org.alfresco.utility.model.UserModel;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "inputtestdata")
public class InputTestData
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

    public TestModel getModelByIndentifier(int id)
    {
        if (folders != null && folders.folders != null)
        {
            for (FolderModel folderModel : folders.folders)
            {
                if (folderModel.getIdentifier() == id)
                {
                    return folderModel;
                }
            }
        }
        
        if(files != null && files.files != null){
            for(FileModel fileModel: files.files){
                 if(fileModel.getIdentifier() == id){
                     return fileModel;
                 }
            }
        }
        
        if(sites != null && sites.sites != null){
            for(SiteModel siteModel: sites.sites){
                if(siteModel.getIdentifier() == id){
                    return siteModel;
                }
            }
        }
        
        if(users != null && users.users != null){
            for(UserModel userModel:users.users){
                if(userModel.getIdentifier() == id){
                    return userModel;
                }
            }
        }

        return null;
    }

    public static class Folders
    {

        protected List<FolderModel> folders;

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

    }

    public static class Files
    {

        protected List<FileModel> files;

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

    }

    public static class Sites
    {

        protected List<SiteModel> sites;

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

    public static class Users
    {

        protected List<UserModel> users;

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

    }

}
