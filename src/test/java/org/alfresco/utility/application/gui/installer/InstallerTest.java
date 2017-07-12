package org.alfresco.utility.application.gui.installer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import java.io.File;
import java.nio.file.Paths;

/**
 * Created by Claudia Agache on 7/10/2017.
 */
@ContextConfiguration("classpath:alfresco-tester-context.xml")
public abstract class InstallerTest extends AbstractTestNGSpringContextTests
{
    @Autowired
    ACSInstaller installer;

    public File createNotEmptyFolderInOS() throws Exception
    {
        File f = (SystemUtils.IS_OS_WINDOWS) ? Paths.get("C:\\tmp\\notEmptyFolder\\subDir").toFile() : Paths.get("/tmp/notEmptyFolder/subDir").toFile();
        if (!f.exists())
        {
            FileUtils.forceMkdir(f);
        }
        return f;
    }
}
