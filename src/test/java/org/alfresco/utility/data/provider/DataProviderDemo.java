package org.alfresco.utility.data.provider;

import org.alfresco.utility.data.DataContent;
import org.alfresco.utility.data.DataSite;
import org.alfresco.utility.data.DataUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

@ContextConfiguration("classpath:alfresco-tester-context.xml")
public class DataProviderDemo extends AbstractTestNGSpringContextTests
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
        testData.cleanup(dataContentService);
    }

    @Test(dataProviderClass = XMLTestDataProvider.class, dataProvider = "getAllData")
    @XMLDataConfig(file = "src/test/resources/data-provider-demo.xml")
    public void prepareEnvironmentData(XMLTestData testData) throws Exception
    {
        this.testData = testData;
        testData.createUsers(userDataService);
        testData.createSitesStructure(dataSiteService, dataContentService, userDataService);
    }
}
