package org.alfresco.utility.data;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class DataProviderReader
{

    public static InputTestData fromXMLFile(InputStream inputStream) throws JAXBException
    {

        JAXBContext context = JAXBContext.newInstance(InputTestData.class);
        Unmarshaller um = context.createUnmarshaller();
        InputTestData dataProvider = (InputTestData) um.unmarshal(inputStream);

        return dataProvider;

    }
}
