package org.alfresco.utility.data;

import java.io.File;

import org.alfresco.utility.LogFactory;
import org.alfresco.utility.model.FileType;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;

public class RandomData
{
    static Logger LOG = LogFactory.getLogger();

    /**
     * Returns a random alphabetic number of 10 characters
     * 
     * @return
     */
    public static String getRandomAlphanumeric()
    {
        String value = RandomStringUtils.randomAlphabetic(10);
        LOG.debug("Generating alphanumeric string: {}", value);
        return value;
    }

    /**
     * @return random folder
     */
    public static File getRandomFolder()
    {
        return new File(getRandomName("folder"));
    }

    /**
     * Returns a random string with a prefix
     * 
     * @param prefix
     * @return random name with a prefix
     */
    public static String getRandomName(String prefix)
    {
        return String.format("%s-%s", prefix, getRandomAlphanumeric());
    }

    /**
     * @param extention
     *            - as "txt", "pdf", "doc"
     * @return random file with <extention>
     */
    public static File getRandomFile(FileType fileType)
    {
        String fileName = String.format("%s.%s", getRandomName("file"), fileType.extention);
        return new File(fileName);
    }

}
