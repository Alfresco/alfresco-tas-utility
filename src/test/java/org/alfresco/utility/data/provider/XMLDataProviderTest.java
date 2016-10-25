package org.alfresco.utility.data.provider;

import org.alfresco.utility.model.FolderModel;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class XMLDataProviderTest
{
    @BeforeClass(alwaysRun = true)
    public void readTestDataFile()
    {
        XMLTestDataProvider.setXmlImputFile("src/test/resources/example-input-data.xml");
    }

    @Test(dataProviderClass = XMLTestDataProvider.class, dataProvider = "getFolders")
    public void getFolders(FolderModel folder)
    {
        System.out.println(folder.getName());
    }

    
    @Test(dataProviderClass = XMLTestDataProvider.class, dataProvider = "getQueries")
    public void getQueries(XMLQuery query)
    {
        
        
        System.out.println(query.getSearch());
        System.out.println(query.getResults());

    }

}
