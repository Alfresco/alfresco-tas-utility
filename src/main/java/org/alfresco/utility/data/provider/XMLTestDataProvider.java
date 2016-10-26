package org.alfresco.utility.data.provider;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.alfresco.utility.exception.DataPreparationException;
import org.alfresco.utility.model.FolderModel;
import org.alfresco.utility.model.QueryModel;
import org.testng.annotations.DataProvider;

public class XMLTestDataProvider
{    
    private static String xmlImputPath;

    private static XMLTestData getXMLTestDataFromFile() throws Exception
    {
        if (getXmlImputFile() == null)
            throw new DataPreparationException(
                    "You didn't defined the Input XML file path for this data provider. Please call setXmlImputFile(...) in a @BeforeClass method");

        JAXBContext context = JAXBContext.newInstance(XMLTestData.class);
        Unmarshaller um = context.createUnmarshaller();

        XMLTestData dataProvider = (XMLTestData) um.unmarshal(getXmlImputFile());
        return dataProvider;
    }

    /**
     * Get all Folder Models from the input xml "*.xml" used as input data in tests
     * 
     * @return iterator over the list of folder model objects
     * @throws Exception
     */
    @DataProvider
    public static Iterator<Object[]> getFolders() throws Exception
    {
        List<Object[]> dataToBeReturned = new ArrayList<Object[]>();

        XMLTestData dataReader = getXMLTestDataFromFile();
        List<FolderModel> folders = dataReader.getFolders();

        for (FolderModel folder : folders)
        {
            dataToBeReturned.add(new Object[] { folder });
        }

        return dataToBeReturned.iterator();
    }
   

    /**
     * Get all Queries from the input xml "*.xml" used as input data in tests
     * 
     * @return iterator over the list of folder model objects
     * @throws Exception
     */
    @DataProvider
    public static Iterator<Object[]> getQueries() throws Exception
    {
        List<Object[]> dataToBeReturned = new ArrayList<Object[]>();

        XMLTestData dataReader = getXMLTestDataFromFile();
        List<QueryModel> queries = dataReader.getQueries();

        for (QueryModel query : queries)
        {
            dataToBeReturned.add(new Object[] { query });
        }

        return dataToBeReturned.iterator();
    }

    public static InputStream getXmlImputFile() throws FileNotFoundException
    {
        return new FileInputStream(xmlImputPath);
    }

    public static void setXmlImputFile(String xmlImputFile)
    {
        XMLTestDataProvider.xmlImputPath = xmlImputFile;
    }
}
