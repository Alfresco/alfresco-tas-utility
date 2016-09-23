package org.alfresco.utility.model;

import org.alfresco.utility.data.RandomData;

public class FileModel extends ContentModel
{
    private String content;
    private FileType fileType;

    public FileModel(String name)
    {
        super(name);
        setFileType(FileType.fromName(name));
    }

    public FileModel(String name, FileType fileType)
    {
        super(name);
        setFileType(fileType);
    }

    public FileModel(String name, String title, String description, FileType fileType)
    {
        super(name, title, description);
        setFileType(fileType);
    }

    public FileModel(String name, FileType fileType, String content)
    {
        this(name, fileType);
        setContent(content);
    }

    public FileModel(String name, String title, String description, FileType fileType, String content)
    {
        this(name, title, description, fileType);
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
    public static FileModel getRandomFileModel(FileType fileType)
    {
        FileModel newFileModel = new FileModel(RandomData.getRandomFile(fileType));
        LOG.info("Generating new FileModel: {}", newFileModel.toString());
        return newFileModel;
    }
}
