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
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
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
        private List<File> files;

        public OSBasedFiles(String osName)
        {
            setOsName(osName);
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
            OSBasedFiles tmp = new OSBasedFiles(osName);
            tmp.setFiles((List<File>) FileUtils.listFiles(parent, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE));
            return tmp;
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
        for (String osName : parent.list())
        {
            osBasedFiles.add(OSBasedFiles.collectOsBasedFiles(osName, Paths.get(parent.getPath(), osName).toFile()));
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("author", System.getProperty("user.name"));
        data.put("osDataFiles", osBasedFiles);

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
