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

@XmlRootElement(name = "testData")
public class XMLTestData
{   
    private List<XMLQuery> queries;

    @XmlElementWrapper   
    @XmlElement(name = "query")
    public List<XMLQuery> getQueries()
    {
        return queries;
    }

    public void setQueries(List<XMLQuery> queries)
    {
        this.queries = queries;
    }

    
     private List<UserModel> users = new ArrayList<UserModel>();
     private List<FolderModel> folders = new ArrayList<FolderModel>();
     private List<FileModel> files = new ArrayList<FileModel>();
     private List<SiteModel> sites = new ArrayList<SiteModel>();
 
    
     @XmlElementWrapper
     @XmlElement(name = "user")
     public List<UserModel> getUsers()
     {
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
     return sites;
     }
    
     public void setSites(List<SiteModel> sites)
     {
     this.sites = sites;
     }
    
}
