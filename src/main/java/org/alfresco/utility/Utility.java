package org.alfresco.utility;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import org.alfresco.dataprep.CMISUtil.DocumentType;
import org.alfresco.utility.data.RandomData;
import org.alfresco.utility.exception.TestConfigurationException;
import org.alfresco.utility.exception.TestObjectNotDefinedException;
import org.alfresco.utility.model.FileType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.json.JSONObject;
import org.slf4j.Logger;

public class Utility
{
    static Logger LOG = LogFactory.getLogger();
    public static int retryCountSeconds = 10;

    public static void checkObjectIsInitialized(Object model, String message) throws Exception
    {
        if (model == null)
            throw new TestObjectNotDefinedException(message);
    }

    public static File getTestResourceFile(String fileName) throws Exception
    {
        LOG.info("Get resource file {}", fileName);

        URL resource = Utility.class.getClassLoader().getResource(fileName);
        if (resource == null)
        {
            throw new TestConfigurationException(String.format("[%s] file was not found in your main resources folder.", fileName));
        }
        return Paths.get(resource.getFile()).toFile();
    }

    public static File getResourceTestDataFile(String fileName) throws Exception
    {
        File tmpFile = RandomData.getRandomFile(FileType.TEXT_PLAIN);
        tmpFile.createNewFile();
        tmpFile.deleteOnExit();

        // TODO: fix this problem
        return tmpFile; // getTestResourceFile("shared-resources/testdata/" + fileName);
    }

    /**
     * @param fileName
     * @return the content of filename found in test/resources/testdata/
     *         <filename>
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

    public static String cmisDocTypeToExtentions(DocumentType cmisDocumentType)
    {
        switch (cmisDocumentType)
        {
            case TEXT_PLAIN:
                return "txt";
            case HTML:
                return "html";
            case MSEXCEL:
                return "xls";
            case MSPOWERPOINT:
                return "ppt";
            case MSWORD:
                return "doc";
            case PDF:
                return "pdf";
            case XML:
                return "xml";
            default:
                break;
        }
        return "txt";
    }

    /**
     * Helper for building strings of the resource passed as parameter
     * 
     * @param parent
     * @param paths
     * @return concatenated paths of <parent> + each <paths>
     */
    public static String buildPath(String parent, String... paths)
    {
        StringBuilder concatenatedPaths = new StringBuilder(parent);
        int lenPaths = paths.length;
        if (lenPaths == 0)
            return concatenatedPaths.toString();

        if (!parent.endsWith("/"))
            concatenatedPaths.append("/");

        for (String path : paths)
        {
            if (!path.isEmpty())
            {
                concatenatedPaths.append(path);
                concatenatedPaths.append("/");
            }
        }
        String concatenated = concatenatedPaths.toString();
        if (lenPaths > 0 && paths[lenPaths - 1].contains("."))
            concatenated = StringUtils.removeEnd(concatenated, "/");
        return concatenated;
    }

    /**
     * If we have
     * "/test/something/now"
     * this method will return "/test/something"
     * 
     * @note the split char is set to "/"
     * @param fullPath
     */
    public static String getParentPath(String fullPath)
    {
        String[] path = fullPath.split("/");
        String fileName = path[path.length - 1];
        return fullPath.replace(fileName, "");
    }

    /**
     * If the path ends with /, methods return the path without last /
     * 
     * @param sourcePath
     * @return sourcePath without last slash
     */
    public static String removeLastSlash(String sourcePath)
    {
        if (StringUtils.endsWith(sourcePath, "/"))
        {
            return StringUtils.removeEnd(sourcePath, "/");
        }
        return sourcePath;
    }

    public static Properties getProperties(Class<?> classz, String propertieFileName) throws TestConfigurationException
    {
        InputStream propsStream = classz.getClassLoader().getResourceAsStream(propertieFileName);
        Properties props = new Properties();
        if (propsStream != null)
        {
            try
            {
                props.load(propsStream);
            }
            catch (Exception e)
            {
                throw new TestConfigurationException(String.format("Cannot load properties from %s file", propertieFileName));
            }
        }
        else
        {
            throw new TestConfigurationException(String.format("Cannot initialize properties from %s file", propertieFileName));
        }

        return props;
    }

    /**
     * We will wait until the <seconds> are passed from current run
     * 
     * @param seconds
     */
    public static void waitToLoopTime(int seconds)
    {
        LOG.info("Waiting (in loops) for: {} second(s).", seconds);
        long currentTime;
        long endTime;
        currentTime = System.currentTimeMillis();
        do
        {
            endTime = System.currentTimeMillis();
        }
        while (endTime - currentTime < (seconds * 1000));
    }

    /**
     * Pretty prints unformatted JSON
     * 
     * @param unformattetJson
     * @return
     */
    public static String prettyPrintJsonString(String unformattetJson)
    {
        JSONObject prettyPrint = new JSONObject(unformattetJson);
        return prettyPrint.toString(3);
    }

    /**
     * Generate URL query string from key value parameters
     * 
     * @param url
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String toUrlParams(String url, Map<String, String> params) throws UnsupportedEncodingException
    {

        List<String> listOfParams = new ArrayList<String>();
        for (String param : params.keySet())
        {
            listOfParams.add(param + "=" + params.get(param));
        }

        if (!listOfParams.isEmpty())
        {
            String queryParam = StringUtils.join(listOfParams, "&");

            int quotePosition = url.indexOf("?");
            int hpos = url.indexOf("#");
            char separator = quotePosition == -1 ? '?' : '&';

            String segment = separator + queryParam;
            return (hpos == -1) ? url + segment : url.substring(0, hpos) + segment + url.substring(hpos);
        }

        return url;
    }

    /**
     * @return the environment property file
     *         if nothing specified, default.properties is used
     *         if -Denvironment=local then local.properties is used.
     */
    public static String getEnvironmentPropertyFile()
    {
        String envPropName = System.getProperty("environment");
        if (envPropName == null)
            envPropName = "default.properties";
        else
            envPropName = String.format("%s.properties", envPropName);
        return envPropName;
    }

}