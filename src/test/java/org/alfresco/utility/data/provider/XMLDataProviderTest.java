package org.alfresco.utility.data.provider;

import javax.xml.datatype.DatatypeConfigurationException;

import org.alfresco.utility.data.DataContent;
import org.alfresco.utility.data.DataSite;
import org.alfresco.utility.data.DataUser;
import org.alfresco.utility.model.FolderModel;
import org.alfresco.utility.model.QueryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import junit.framework.Assert;

@ContextConfiguration("classpath:alfresco-tester-context.xml")
public class XMLDataProviderTest extends AbstractTestNGSpringContextTests
{

    @Autowired
    DataUser userDataService;

    @Autowired
    DataSite dataSiteService;

    @Autowired
    DataContent dataContentService;

    XMLTestData testData;

    @AfterClass(alwaysRun = true)
    public void cleanupEnvironment() throws Exception
    {
        //testData.cleanup(dataContentService);
    }

    @Test(dataProviderClass = XMLTestDataProvider.class, dataProvider = "getAllData")
    @XMLDataConfig(file = "src/test/resources/example-input-data.xml")
    public void prepareEnvironmentData(XMLTestData testData) throws Exception
    {
        this.testData = testData;
        testData.createUsers(userDataService);
        testData.createSitesStructure(dataSiteService, dataContentService, userDataService);

    }

    @Test(dataProviderClass = XMLTestDataProvider.class, dataProvider = "getUsersData")
    @XMLDataConfig(file = "src/test/resources/example-input-data.xml")
    public void getUsersData(XMLUserData user)
    {
        // i can navigate on all users
        System.out.println(user.getName());
    }

    @Test(dataProviderClass = XMLTestDataProvider.class, dataProvider = "getSitesData")
    @XMLDataConfig(file = "src/test/resources/example-input-data.xml")
    public void getSitesData(XMLSiteData siteData)
    {
        // i can navigate on all sites
        System.out.println(siteData.getCreatedBy());
        for (XMLFolderData folder : siteData.getFolders())
        {
            System.out.println(folder.getName());
        }
    }

    @Test(dataProviderClass = XMLTestDataProvider.class, dataProvider = "getQueriesData")
    @XMLDataConfig(file = "src/test/resources/example-input-data.xml")
    public void getQueries(QueryModel query)
    {
        // i can navigate on all queries
        System.out.println(query.getResults());
        System.out.println(query.getValue());

    }

    @Test(dependsOnMethods = "prepareEnvironmentData")    
    public void iCanIdentifyTestDataByID() throws DatatypeConfigurationException
    {
        FolderModel f1 = (FolderModel) testData.getTestDataItemWithId("folder-1").getModel();
        Assert.assertEquals(f1.getName(), "folder1");
    }

}
