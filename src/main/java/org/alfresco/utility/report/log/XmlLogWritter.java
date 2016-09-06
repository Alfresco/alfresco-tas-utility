package org.alfresco.utility.report.log;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.alfresco.utility.LogFactory;
import org.alfresco.utility.Utility;
import org.slf4j.Logger;
import org.testng.ITestContext;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlLogWritter
{
    Logger LOG = LogFactory.getLogger();
    private boolean configurationError = true;
    Properties logProperties = new Properties();
    private String logPath;
    private String fullPath;

    public XmlLogWritter()
    {
        InputStream defaultProp = getClass().getClassLoader().getResourceAsStream("default.properties");
        if (defaultProp != null)
        {
            try
            {
                logProperties.load(defaultProp);
                this.logPath = logProperties.getProperty("log.path");
                Utility.checkObjectIsInitialized(logPath, "logPath");
                configurationError = false;
            }
            catch (Exception e)
            {
                LOG.error("Cannot initialize Log Management Settings from default.properties file");
            }
        }
    }

    public boolean hasConfigurationErrors()
    {
        return configurationError;
    }

    public void generateXmlFile(ITestContext context)
    {
        if (hasConfigurationErrors())
            return;
        try
        {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("results");
            doc.appendChild(rootElement);

            Element resultName = doc.createElement("name");
            resultName.appendChild(doc.createTextNode(context.getName()));
            rootElement.appendChild(resultName);

            Element resultStartDate = doc.createElement("date");
            resultStartDate.appendChild(doc.createTextNode(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(context.getStartDate())));
            rootElement.appendChild(resultStartDate);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            fullPath = logPath + File.separator + context.getName() + "-" + new SimpleDateFormat("yyyy-MM-dd_HHmmss").format(context.getStartDate()) + ".xml";
            
            StreamResult result = new StreamResult(new File(fullPath));
            transformer.transform(source, result);
            System.out.println("File saved!");
        }
        catch (ParserConfigurationException pce)
        {
            pce.printStackTrace();
        }
        catch (TransformerException tfe)
        {
            tfe.printStackTrace();
        }
    }

}
