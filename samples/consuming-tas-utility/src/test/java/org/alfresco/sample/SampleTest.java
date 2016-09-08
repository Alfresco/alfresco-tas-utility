package org.alfresco.sample;

import org.alfresco.utility.ServerHealth;
import org.alfresco.utility.data.DataContent;
import org.alfresco.utility.data.DataSite;
import org.alfresco.utility.data.DataUser;
import org.alfresco.utility.model.FolderModel;
import org.alfresco.utility.model.SiteModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@ContextConfiguration("classpath:alfresco-test-context.xml")
public class SampleTest extends AbstractTestNGSpringContextTests
{
    @Autowired
    protected ServerHealth serverHealth;

    @Autowired
    protected DataUser dataUser;

    @Autowired
    protected DataSite dataSite;

    @Autowired
    protected DataContent dataContent;

    protected SiteModel testSite;
    protected FolderModel folderModel;

    @BeforeClass(alwaysRun = true)
    public void setupCifsTest() throws Exception
    {
        serverHealth.assertServerIsOnline();
        testSite = dataSite.createPublicRandomSite();
        folderModel = FolderModel.getRandomFolderModel();
    }

    @Test
    public void creatingFolderInRepoLocation() throws Exception
    {
        /*
         * this call will create folder 'myTest' under
         * '/Sites/<randomPublicSiteName>/documentLibrary' location using default admin user
         * specified in default.properties file
         */
        dataContent.usingSite(testSite).createFolder(folderModel.getName());

        /*
         * this call will create folder 'myTest2' under '/' root folder using
         * default admin user specified in default.properties file
         */
        FolderModel myFolder = dataContent.usingRoot().createFolder("MyTestFolder");
        dataContent.assertContentExist(myFolder);
    }
}
