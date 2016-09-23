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
}
