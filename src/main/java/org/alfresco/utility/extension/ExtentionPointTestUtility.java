package org.alfresco.utility.extension;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.alfresco.utility.exception.XMLToModelUnmarshalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

@Service
@Scope(value = "prototype")
public class ExtentionPointTestUtility
{

    static Logger LOGGER = LoggerFactory.getLogger(ExtentionPointTestUtility.class);

    /**
     * convert XML data to a java content tree
     * 
     * @param importFileXML
     * @return
     * @throws JAXBException
     */
    public ExtensionPointTestSuite xmlToClass(InputStream importFileXML) throws JAXBException
    {

        JAXBContext context = JAXBContext.newInstance(ExtensionPointTestSuite.class);
        Unmarshaller um = context.createUnmarshaller();
        ExtensionPointTestSuite webScriptController = (ExtensionPointTestSuite) um.unmarshal(importFileXML);

        return webScriptController;
    }

    /**
     * validate xml file against a standard schema
     * 
     * @param importFileXML
     * @throws IOException
     * @throws XMLToModelUnmarshalException
     */
    public void validateAgainstXSD(InputStream importFileXML) throws IOException, XMLToModelUnmarshalException
    {
        try
        {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(getWebScriptXSDSchemaFile()));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(importFileXML));
        }
        catch (SAXException ex)
        {
            throw new XMLToModelUnmarshalException(ExtentionPointTestUtility.class, ex);
        }
    }

    /**
     * return standard schema from resources
     * 
     * @return
     */
    private File getWebScriptXSDSchemaFile()
    {
        // Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        File schemaFile = new File(classLoader.getResource("extension/webscript/extentionPointTestSuiteTemplate.xsd").getFile());
        return schemaFile;
    }

}
