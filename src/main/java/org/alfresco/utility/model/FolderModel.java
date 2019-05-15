package org.alfresco.utility.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.alfresco.utility.data.RandomData;

@XmlType(name = "folder")
public class FolderModel extends ContentModel
{
    protected List<FileModel> files = new ArrayList<FileModel>();    
    public FolderModel()
    {
    }

    public FolderModel(String name)
    {
        super(name);
    }

    public FolderModel(String name, String title, String description)
    {
        super(name, title, description);
    }
    
    public FolderModel(FolderModel originalFolder)
    {
        this(originalFolder.getName(), originalFolder.getTitle(), originalFolder.getDescription());
        setCmisLocation(originalFolder.getCmisLocation());
        setProtocolLocation(originalFolder.getProtocolLocation());
    }

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

    /**
     * Generates a new random {@link FolderModel} object
     */
    public static FolderModel getRandomFolderModel()
    {
        FolderModel model = new FolderModel(RandomData.getRandomFolder());
        LOG.info("Generating new FolderModel: {}", model.toString());
        return model;
    }

    /**
     * @return the last {@link FileModel} object child
     */
    public FileModel lastFile()
    {
        return getFiles().get(getFiles().size() - 1);
    }

    /**
     * This will generate and add to {@link #getFiles()} array the children files
     */
    public FileModel generateRandomFileChild(FileType fileType)
    {
        FileModel model = FileModel.getRandomFileModel(fileType);
        getFiles().add(model);
        return model;
    }

    public static FolderModel getSharedFolderModel()
    {
        return new FolderModel("/Shared");
    }

    public static FolderModel getImapAttachmentsFolderModel()
    {
        return new FolderModel("/Imap Attachments");
    }

    public static FolderModel getGuestHomeFolderModel()
    {
        return new FolderModel("/Guest Home");
    }

    public static FolderModel getUserHomesFolderModel()
    {
        return new FolderModel("/User Homes");
    }

    public static FolderModel getSitesFolderModel()
    {
        return new FolderModel("/Sites");
    }

    public static FolderModel getDataDictionaryFolderModel()
    {
        return new FolderModel("/Data Dictionary");
    }

    public static FolderModel getIMAPHomeFolderModel()
    {
        return new FolderModel("/IMAP Home");
    }
}
