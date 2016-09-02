package org.alfresco.utility.model;

import java.io.File;
import java.nio.file.Paths;

import org.alfresco.utility.data.RandomData;
import org.apache.commons.lang3.RandomStringUtils;

public class FileModel extends ContentModel
{
    private String content;
    private FileType fileType;

    public FileModel(String location)
    {
    	super(location);
    	setFileType(FileType.fromPath(getLocation()));
    }
    
    public FileModel(File location)
    {
        super(location);
        setFileType(FileType.fromPath(location.getPath()));
    }

    public FileModel(FileType fileType, File location)
    {
        super(location);
        setFileType(fileType);
    }

    public FileModel(FileType fileType, File location, String title)
    {
        super(location, title);
        setFileType(fileType);
    }

    public FileModel(FileType fileType, File location, String title, String description)
    {
        super(location, title, description);
        setFileType(fileType);
    }

    public FileModel(FileType fileType, File location, String title, String description, String content)
    {
        this(fileType, location, title, description);
        setContent(content);
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public FileType getFileType()
    {
        return fileType;
    }

    public void setFileType(FileType fileType)
    {
        this.fileType = fileType;
    }

    /**
     * Generates a new random {@link FileModel} object
     */
    public static FileModel getRandomFileModel(FileType fileType, String parent)
    {
        File location = Paths.get(parent, String.format("file-%s.%s", RandomStringUtils.randomAlphanumeric(10), fileType.extention)).toFile();
        FileModel newFileModel = new FileModel(location);
        LOG.info("Generating new FileModel: {}", newFileModel.toString());
        return newFileModel;
    }

    /**
     * Generates a new random {@link FileModel} object
     */
    public static FileModel getRandomFileModel(FileType fileType)
    {
        FileModel newFileModel = new FileModel(RandomData.getRandomFile(fileType));
        LOG.info("Generating new FileModel: {}", newFileModel.toString());
        return newFileModel;
    }
}
