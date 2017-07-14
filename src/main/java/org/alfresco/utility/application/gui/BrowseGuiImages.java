package org.alfresco.utility.application.gui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.utility.Utility;
import org.alfresco.utility.exception.TestConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.SystemUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Running this class, you will see in browser all imates collected
 * 
 * @author Paul Brodner
 */
public class BrowseGuiImages
{
    private static Configuration cfg;

    public static class OSBasedFiles
    {
        private String osName;
        private File parent;
        private List<File> files = new ArrayList<File>();

        public OSBasedFiles(String osName, File parent)
        {
            setOsName(osName);
            setParent(parent);
        }

        public String getOsName()
        {
            return osName;
        }

        public void setOsName(String osName)
        {
            this.osName = osName;
        }

        public List<File> getFiles()
        {
            return files;
        }

        public String[] getArrayFiles()
        {
            ArrayList<String> tmp = new ArrayList<String>();
            for (File f : files)
            {
                tmp.add(f.getPath().split(osName)[1]);
            }
            return tmp.toArray(new String[tmp.size()]);
        }

        public String getFilesCount()
        {
            return String.valueOf(files.size());
        }

        public void setFiles(List<File> files)
        {
            this.files = files;
        }

        public static OSBasedFiles collectOsBasedFiles(String osName, File parent)
        {
            OSBasedFiles tmp = new OSBasedFiles(osName, parent);

            File fromLocation = Paths.get(parent.getPath(), osName).toFile();
            if (fromLocation.isDirectory())
            {
                tmp.setFiles((List<File>) FileUtils.listFiles(fromLocation, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE));
            }

            return tmp;
        }

        public String fileExist(String filename) throws TestConfigurationException
        {
            File file = Paths.get(getParent().getPath(), getOsName(), filename).toFile();
            if (file.exists())
            {
                return file.getPath();
            }
            else
                return "";//Utility.getTestResourceFile("shared-resources/gui/x.png").getPath();
        }

        public File getParent()
        {
            return parent;
        }

        public void setParent(File parent)
        {
            this.parent = parent;
        }
    }

    public static void main(String[] args) throws Exception
    {
        String imgPath = System.getProperty("imgPath");
        if (imgPath == null)
            imgPath = "shared-resources/gui";

        System.out.println("Collect GUI Images form:" + imgPath);
        Template browseImagesTemplate = getConfig().getTemplate("shared-resources/report/browse-images.ftl");

        File parent = Utility.getTestResourceFile(imgPath);
        List<OSBasedFiles> osBasedFiles = new ArrayList<OSBasedFiles>();

        String[] uniqueOSBasedFiles = new String[] {};

        for (String osName : parent.list())
        {
            OSBasedFiles tmp = OSBasedFiles.collectOsBasedFiles(osName, parent);

            osBasedFiles.add(tmp);
            uniqueOSBasedFiles = (String[]) ArrayUtils.addAll(uniqueOSBasedFiles, tmp.getArrayFiles());
        }
        
        

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("author", System.getProperty("user.name"));
        data.put("osDataFiles", osBasedFiles);
        data.put("uniqueOSBasedFiles", uniqueOSBasedFiles);

        Writer append = new StringWriter();
        browseImagesTemplate.process(data, append);
        append.close();

        File output = Paths.get("image-browser.html").toFile();
        FileWriter fw = new FileWriter(output);
        fw.write(append.toString());
        fw.close();

        if (SystemUtils.IS_OS_UNIX || SystemUtils.IS_OS_MAC)
        {
            Utility.executeOnUnix("open " + output.getPath());
        }
        else
        {
            Utility.executeOnWin("start " + output.getPath());
        }
    }

    private static Configuration getConfig() throws IOException
    {
        if (cfg == null)
        {
            cfg = new Configuration(Configuration.VERSION_2_3_23);
            cfg.setDirectoryForTemplateLoading(new File("src/main/resources"));
        }
        return cfg;
    }
}
