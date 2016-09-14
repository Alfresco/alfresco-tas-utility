package org.alfresco.utility.model;

import java.io.File;
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

    public FolderModel(File location)
    {
        super(location);
    }

    public FolderModel(File location, String title)
    {
        super(location, title);
    }

    public FolderModel(File location, String title, String description)
    {
        super(location, title, description);
    }

    /**
     * Generate a new random FileModel object
     * 
     * @param fileType
     * @return FileModel inside this folder
     */
    public FolderModel generateRandomFileChild(FileType fileType)
    {
        addFileAsChild(FileModel.getRandomFileModel(fileType, getLocation()));
        return this;
    }

    public FolderModel addFileAsChild(FileModel fileModel)
    {
        getFiles().add(fileModel);
        return this;
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

    public FileModel lastFile()
    {
        return getFiles().get(getFiles().size() - 1);
    }

    /**
     * Generates a new random {@link FolderModel} object
     */
    public static FolderModel getRandomFolderModel()
    {
        FolderModel model = new FolderModel(RandomData.getRandomFolder().getName());
        LOG.info("Generating new FolderModel: {}", model.toString());
        return model;
    }

}
