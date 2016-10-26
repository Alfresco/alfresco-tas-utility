package org.alfresco.utility.data.provider;

import org.alfresco.utility.model.QueryModel;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class XMLDataProviderTest
{
    @BeforeClass(alwaysRun = true)
    public void readTestDataFile()
    {
        XMLTestDataProvider.setXmlImputFile("src/test/resources/example-input-data.xml");
    }

    @Test(dataProviderClass = XMLTestDataProvider.class, dataProvider = "getSitesData")
    public void prepareEnvironment(XMLSiteData siteData)
    {
        System.out.println(siteData.getCreatedBy());
    }

    @Test(dataProviderClass = XMLTestDataProvider.class, dataProvider = "getQueriesData")
    public void getQueries(QueryModel query)
    {
                
        System.out.println(query.getResults());
        System.out.println(query.getValue());

    }

}
