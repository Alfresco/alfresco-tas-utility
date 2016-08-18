package org.alfresco.utility;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.alfresco.utility.exception.TestConfigurationException;
import org.alfresco.utility.exception.TestObjectNotDefinedException;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;

public class Utility
{
    static Logger LOG = LogFactory.getLogger();

    public static void checkObjectIsInitialized(Object model, String message) throws Exception
    {
        if (model == null)
            throw new TestObjectNotDefinedException(message);
    }

    @SuppressWarnings("unused")
    public static File getResourceTestDataFile(String fileName) throws Exception
    {
        LOG.info("Get resource test/resources/testdata/{}", fileName);
        File resource = new File(Utility.class.getClassLoader().getResource(fileName).getFile());
        if (resource == null)
        {
            throw new TestConfigurationException(String.format("Your file %s was not found in test/resources/testdata folder", fileName));
        }

        return resource;
    }

    /**
     * @param fileName
     * @return the content of filename found in test/resources/testdata/<filename>
     * @throws Exception
     */
    public static String getResourceTestDataContent(String fileName) throws Exception
    {
        StringBuilder result = new StringBuilder("");
        File file = getResourceTestDataFile(fileName);

        try (Scanner scanner = new Scanner(file))
        {
            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }
            scanner.close();
        }
        catch (IOException e)
        {
            throw new TestConfigurationException(String.format("Cannot read from file %s. Error thrown: %s", fileName, e.getMessage()));
        }
        return result.toString();
    }
    
    public static String convertBackslashToSlash(String value)
    {
        if (SystemUtils.IS_OS_WINDOWS)
        {
            value = value.replace("\\", "/");
        }

        return value;
    }
}