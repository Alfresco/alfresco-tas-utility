package org.alfresco.utility.data.provider;

import org.alfresco.utility.data.DataContent;
import org.alfresco.utility.data.DataSite;
import org.alfresco.utility.data.DataUser;
import org.alfresco.utility.model.QueryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@ContextConfiguration("classpath:alfresco-tester-context.xml")
public class XMLDataProviderTest extends AbstractTestNGSpringContextTests
{
    @BeforeClass(alwaysRun = true)
    public void readTestDataFile()
    {
        XMLTestDataProvider.setXmlImputFile("src/test/resources/example-input-data.xml");
    }

    @Autowired
    DataUser userDataService;

    @Autowired
    DataSite dataSiteService;

    @Autowired
    DataContent dataContentService;

    XMLTestData testData;

    //@AfterClass(alwaysRun = true)
    public void cleanupEnvironment() throws Exception
    {
        testData.cleanup(dataContentService);
    }

    //@Test(priority = 0, dataProviderClass = XMLTestDataProvider.class, dataProvider = "prepareEnvironmentData")
    public void prepareEnvironmentData(XMLTestData testData) throws Exception
    {
        this.testData = testData;
        testData.createUsers(userDataService);
        testData.createSitesStructure(dataSiteService, dataContentService);
    }
    
    @Test(dataProviderClass = XMLTestDataProvider.class, dataProvider = "getUsersData")
    public void getUsersData(XMLUserData user)
    {
        System.out.println(user.getName());
    }

    @Test(dataProviderClass = XMLTestDataProvider.class, dataProvider = "getSitesData")
    public void getSitesData(XMLSiteData siteData)
    {
        System.out.println(siteData.getCreatedBy());
        for (XMLFolderData folder : siteData.getFolders())
        {
            System.out.println(folder.getName());
        }
    }

    @Test(dataProviderClass = XMLTestDataProvider.class, dataProvider = "getQueriesData")
    public void getQueries(QueryModel query)
    {

        System.out.println(query.getResults());
        System.out.println(query.getValue());

    }

}
