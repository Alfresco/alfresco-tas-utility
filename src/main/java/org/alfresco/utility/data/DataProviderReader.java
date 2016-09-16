package org.alfresco.utility.data;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.alfresco.utility.model.FolderModel;
import org.testng.annotations.DataProvider;

public class DataProviderReader
{

    private static InputTestData fromXMLFile(InputStream inputStream) throws JAXBException
    {

        JAXBContext context = JAXBContext.newInstance(InputTestData.class);
        Unmarshaller um = context.createUnmarshaller();
        InputTestData dataProvider = (InputTestData) um.unmarshal(inputStream);

        return dataProvider;

    }

    /**
     * Get all Folder Models from the input xml "input-data.xml" used as iput data in tests
     * 
     * @return iterator over the list of folder model objects
     * @throws Exception
     */
    @DataProvider
    public static Iterator<Object[]> getFolder() throws Exception
    {
        List<Object[]> dataToBeReturned = new ArrayList<Object[]>();

        InputTestData dataReader = DataProviderReader.fromXMLFile(DataProviderReader.class.getClass().getResourceAsStream("/input-data.xml"));
        List<FolderModel> folders = dataReader.getFolders();

        for (FolderModel folder : folders)
        {
            dataToBeReturned.add((Object[]) new Object[] { folder });
        }

        return dataToBeReturned.iterator();
    }
}
