package org.alfresco.utility.data;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class DataProviderReader
{

    public static Data xmlDataProviderToJava(InputStream inputStream) throws JAXBException
    {

        JAXBContext context = JAXBContext.newInstance(Data.class);
        Unmarshaller um = context.createUnmarshaller();
        Data dataProvider = (Data) um.unmarshal(inputStream);

        return dataProvider;

    }
}
