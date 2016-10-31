package org.alfresco.utility.data.provider;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.alfresco.utility.exception.DataPreparationException;
import org.alfresco.utility.model.QueryModel;
import org.testng.annotations.DataProvider;

/**
 * 
 * Provides test data providers based on XML input file
 * 
 * @author Paul Brodner
 *
 */
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
     * @return iterator over the list of List<XMLSiteData> sites data
     * @throws Exception
     */
    @DataProvider
    public static Iterator<Object[]> getSitesData() throws Exception
    {
        List<Object[]> dataToBeReturned = new ArrayList<Object[]>();

        XMLTestData dataReader = getXMLTestDataFromFile();
        List<XMLSiteData> sites = dataReader.getSites();

        for (XMLSiteData site : sites)
        {
            dataToBeReturned.add(new Object[] { site });
        }

        return dataToBeReturned.iterator();
    }

    @DataProvider
    public static Iterator<Object[]> getUsersData() throws Exception
    {
        List<Object[]> dataToBeReturned = new ArrayList<Object[]>();

        XMLTestData dataReader = getXMLTestDataFromFile();
        List<XMLUserData> users = dataReader.getUsers();

        for (XMLUserData user : users)
        {
            dataToBeReturned.add(new Object[] { user });
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
    public static Iterator<Object[]> getQueriesData() throws Exception
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

    /**
     * Return the data for your environment
     * 
     * Calling {@link XMLTestData#create()} will generate via CMIS entire structure in alfresco
     * @return {@link XMLTestData}
     * @throws Exception
     */
    @DataProvider
    public static Object[][] prepareEnvironmentData(Method m) throws Exception
    { 
        String file = (m.getAnnotation(DataProviderFile.class)).file();
        setXmlImputFile(file);
        XMLTestData dataFromXMLFile = getXMLTestDataFromFile();
        return  new Object[][] { {dataFromXMLFile }};
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
