package org.alfresco.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

import org.alfresco.dataprep.CMISUtil.DocumentType;
import org.alfresco.utility.exception.TestConfigurationException;
import org.alfresco.utility.exception.TestObjectNotDefinedException;
import org.alfresco.utility.testrail.TestRailExecutorListener;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.json.JSONObject;
import org.slf4j.Logger;

public class Utility
{
    static Logger LOG = LogFactory.getLogger();
    public static int retryCountSeconds = 15;

    public static void checkObjectIsInitialized(Object model, String message) throws Exception
    {
        if (model == null)
            throw new TestObjectNotDefinedException(message);
    }

    /**
     * Return a file from the filePath location
     * 
     * @param filePath
     * @return file object
     * @throws Exception
     */
    public static File getTestResourceFile(String filePath) throws TestConfigurationException
    {
        LOG.info("Get resource file {}", filePath);

        URL resource = Utility.class.getClassLoader().getResource(filePath);
        if (resource == null)
        {
            throw new TestConfigurationException(String.format("[%s] file was not found in your main resources folder.", filePath));
        }

        return new File(resource.getFile());
    }

    public static File getResourceTestDataFile(String fileName) throws TestConfigurationException
    {
        return getTestResourceFile("shared-resources/testdata/" + fileName);
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
                if (path.substring(0, 1).equals("/"))
                {
                    path = path.replaceFirst("/", "");
                }
                if (path.endsWith("/"))
                {
                    path = path.substring(0, path.length() - 1);
                }
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
     * Check if property identified by key in *.properties file is enabled or not
     * 
     * @param key
     * @return
     */
    public static boolean isPropertyEnabled(String key)
    {
        boolean isEnabled = false;
        Properties properties = new Properties();
        try
        {
            properties = Utility.getProperties(TestRailExecutorListener.class, Utility.getEnvironmentPropertyFile());
            isEnabled = Boolean.valueOf(Utility.getSystemOrFileProperty(key, properties));
        }
        catch (TestConfigurationException e1)
        {
            System.err.println("Cannot read properties from '" + Utility.getEnvironmentPropertyFile() + "'. Error: " + e1.getMessage());
        }

        return isEnabled;
    }

    /**
     * We will wait until the <seconds> are passed from current run
     * 
     * @param seconds
     */
    public static void waitToLoopTime(int seconds, String... info)
    {
        LOG.info("Wait until {} second(s) are passed. {}", seconds, StringUtils.join(info, ' '));
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

    public static String splitGuidVersion(String guidWithVersion)
    {
        if (guidWithVersion != null && guidWithVersion.contains(";"))
            return guidWithVersion.split(";")[0];

        return guidWithVersion;
    }

    /**
     * Search recursively in path
     * 
     * @param fileName
     * @param path
     */
    public static File checkFileInPath(String fileName, String path)
    {
        File directory = new File(path);
        File[] list = directory.listFiles();

        if (list == null)
            return null;

        for (File f : list)
        {
            if (f.isDirectory())
            {
                checkFileInPath(fileName, f.getAbsolutePath());
            }
            else
            {
                if (FilenameUtils.getBaseName(f.getName()).equals(fileName))
                {
                    return f;
                }
            }
        }
        return null;
    }

    /**
     * Create a new {@link File} with specific size (MB)
     * 
     * @param fileName
     * @param sizeMB
     * @return {@link File}
     * @throws Exception
     */
    public static File getFileWithSize(String fileName, int sizeMB) throws Exception
    {
        byte[] buffer = getRandomString(1024 * 1024).getBytes();
        int number_of_lines = sizeMB;
        @SuppressWarnings("resource")
        FileChannel rwChannel = new RandomAccessFile(fileName, "rw").getChannel();
        ByteBuffer wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, 0, buffer.length * number_of_lines);
        for (int i = 0; i < number_of_lines; i++)
        {
            wrBuf.put(buffer);
        }
        rwChannel.close();
        File file1 = new File(fileName);
        return file1;
    }

    private static String getRandomString(int length)
    {
        Random RANDOM = new Random();
        char from[] = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++)
        {
            result.append(from[RANDOM.nextInt((from.length - 1))]);
        }
        return result.toString();
    }

    public static String getSystemOrFileProperty(String key, Properties properties)
    {
        String value = System.getProperty(key);
        if (value == null)
        {
            return properties.getProperty(key);
        }
        else
            return value;
    }

    /**
     * Execute any Terminal commands
     * 
     * Example:
     * executeOnWin("ls -la")
     * @param command
     * @return
     */
    public static String executeOnUnix(String command)
    {
        LOG.info("On Unix execute command: [{}]", command);

        StringBuilder sb = new StringBuilder();
        String[] commands = new String[] { "/bin/sh", "-c", command };
        try
        {
            Process proc = new ProcessBuilder(commands).start();
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

            String s = null;
            while ((s = stdInput.readLine()) != null)
            {
                sb.append(s);
                sb.append("\n");
            }

            while ((s = stdError.readLine()) != null)
            {
                sb.append(s);
                sb.append("\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * Example:
     * executeOnWin("mkdir 'a'")
     * @param command
     * @return the List of lines returned by command
     */
    public static String executeOnWin(String command)
    {
        LOG.info("On Windows execute command: [{}]", command);
        
        List<String> lines = new ArrayList<String>();
        try
        {
            Process p = Runtime.getRuntime().exec("cmd /c " + command);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null)
            {
                if (!line.startsWith(" Volume"))
                {
                    lines.add(line);
                }
            }
        }
        catch (IOException e1)
        {
        }
        catch (InterruptedException e2)
        {
        }
        return Arrays.toString(lines.toArray());
    }
}