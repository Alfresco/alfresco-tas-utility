package org.alfresco.utility.data.provider;

import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.alfresco.utility.Utility;
import org.alfresco.utility.exception.DataPreparationException;
import org.alfresco.utility.model.QueryModel;
import org.testng.annotations.DataProvider;

/**
 * 
 * Provides test data providers based on XML input file
 * 
 * This class is working in correlation with {@link XMLDataConfig} annotation
 * @author Paul Brodner
 *
 */
public class XMLTestDataProvider
{
    private static XMLTestData initializeXMLFileData(Method m) throws Exception
    {
        XMLDataConfig config = (m.getAnnotation(XMLDataConfig.class));
        if(config==null)
        {            
            throw new DataPreparationException("Please annotate your test that is using XMLTestDataProvider with @XMLDataConfig(file='../location-to-your-xml-test-data-file.xml'");
        }
        
        JAXBContext context = JAXBContext.newInstance(XMLTestData.class);
        Unmarshaller um = context.createUnmarshaller();
       
        XMLTestData dataProvider = (XMLTestData) um.unmarshal(new FileInputStream(config.file()));
        return dataProvider;
    }

    /**
     * Get all Folder Models from the input xml "*.xml" used as input data in tests
     * 
     * @return iterator over the list of List<XMLSiteData> sites data
     * @throws Exception
     */
    @DataProvider
    public static Iterator<Object[]> getSitesData(Method m) throws Exception
    {
        List<Object[]> dataToBeReturned = new ArrayList<Object[]>();

        XMLTestData dataReader = initializeXMLFileData(m);
        List<XMLSiteData> sites = dataReader.getSites();

        for (XMLSiteData site : sites)
        {
            dataToBeReturned.add(new Object[] { site });
        }

        return dataToBeReturned.iterator();
    }

    @DataProvider
    public static Iterator<Object[]> getUsersData(Method m) throws Exception
    {
        List<Object[]> dataToBeReturned = new ArrayList<Object[]>();

        XMLTestData dataReader = initializeXMLFileData(m);
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
    public static Iterator<Object[]> getQueriesData(Method m) throws Exception
    {
        List<Object[]> dataToBeReturned = new ArrayList<Object[]>();

        XMLTestData dataReader = initializeXMLFileData(m);
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
    public static Object[][] getAllData(Method m) throws Exception
    {        
        XMLTestData dataFromXMLFile = initializeXMLFileData(m);
        return  new Object[][] { {dataFromXMLFile }};
    }
    
}
