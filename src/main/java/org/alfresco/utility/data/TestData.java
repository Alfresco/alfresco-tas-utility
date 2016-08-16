package org.alfresco.utility.data;

import java.io.File;

import org.alfresco.utility.LogFactory;
import org.alfresco.utility.TasProperties;
import org.alfresco.utility.model.FileModel;
import org.alfresco.utility.model.FileType;
import org.alfresco.utility.model.FolderModel;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.io.Files;

public abstract class TestData
{
    static Logger LOG = LogFactory.getLogger();

    @Autowired
    protected TasProperties tasProperties;

    protected static String PASSWORD = "password";
    protected static String EMAIL = "%s@tas-automation.org";
    protected static String USER = "user";

    public static String getRandomAlphanumeric()
    {
        String value = RandomStringUtils.randomAlphabetic(10);
        LOG.info("Generating alphanumeric string: {}", value);
        return value;
    }

    /**
     * Check if <filename> passed as parameter is a file or not based on extention
     */
    public static boolean isAFile(String filename)
    {
        return Files.getFileExtension(filename).length() == 3;
    }

    /**
     * @param extention - as "txt", "pdf", "doc"
     * @return random file with <extention>
     */
    public File getRandomFile(String extention)
    {
        String fileName = String.format("file-%s%s", RandomStringUtils.randomAlphanumeric(10), extention);
        return new File(fileName);
    }

    public FileModel generateRandomFilePathModel(FileType fileType)
    {
        FileModel model = new FileModel(fileType, getRandomFile(fileType.extention));
        LOG.info("Generating new Model: {}", model.toString());
        return model;
    }

    public FolderModel generateRandomFolderPathModel()
    {
        FolderModel model = new FolderModel(getRandomFolder());
        LOG.info("Generating new Model: {}", model.toString());
        return model;
    }

    /**
     * @return random folder
     */
    public File getRandomFolder()
    {
        String folderName = String.format("folder-%s", RandomStringUtils.randomAlphanumeric(10));
        return new File(folderName);
    }
}
