
package org.alfresco.utility.listener;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.alfresco.utility.LogFactory;
import org.slf4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Writer class to generate XML tests suite.
 * 
 * @author Cristina Diaconu
 */
public class XmlTestsSuiteWriter
{
    public static Logger LOG = LogFactory.getLogger();
    private static final String SUITE_NAME = "Tests for Test Group = ";

    /**
     * Generate the XML file suite and write it to the disk.
     * 
     * @param xmlFileName The XML file name.
     * @param testClasses The map containing the classes and methods to be write on the XML file.
     */
    public void generateXmlFile(String xmlFileName, Map<String, List<String>> testClasses, String testGroup)
    {
        try
        {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            doc.setXmlStandalone(true);

            generateXMLBody(doc, testClasses, testGroup);

            // write the content into XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://testng.org/testng-1.0.dtd");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);

            StreamResult result = new StreamResult(new File(xmlFileName));
            transformer.transform(source, result);
        }
        catch (Exception e)
        {
            LOG.error("Cannot create the xml file log. Error: {}", e.getMessage());
        }
    }

    private void generateXMLBody(Document doc, Map<String, List<String>> testClasses, String testGroup)
    {
        // create Suite element
        Element rootElement = doc.createElement("suite");
        doc.appendChild(rootElement);

        // add Suite -> Name attribute
        Attr suiteName = doc.createAttribute("name");
        suiteName.setValue(SUITE_NAME + testGroup);
        rootElement.setAttributeNode(suiteName);

        // add Suite -> Verbose attribute
        Attr verboseAttr = doc.createAttribute("verbose");
        verboseAttr.setValue("1");
        rootElement.setAttributeNode(verboseAttr);

        // create Test element
        Element testElement = doc.createElement("test");
        rootElement.appendChild(testElement);

        // add Test -> Name attribute
        Attr testNameAttr = doc.createAttribute("name");
        testNameAttr.setValue("Tas sanity suite");
        testElement.setAttributeNode(testNameAttr);

        // create Classes element
        Element classesElement = doc.createElement("classes");
        testElement.appendChild(classesElement);

        for (String className : testClasses.keySet())
        {
            // create Class element
            Element classElement = doc.createElement("class");
            classesElement.appendChild(classElement);

            // add Class -> Name attribute
            Attr classNameAttr = doc.createAttribute("name");
            classNameAttr.setValue(className);
            classElement.setAttributeNode(classNameAttr);

            // create Methods element
            Element methodsElement = doc.createElement("methods");
            classElement.appendChild(methodsElement);

            for (String methodName : testClasses.get(className))
            {
                // create Include element
                Element includeElement = doc.createElement("include");
                methodsElement.appendChild(includeElement);

                // add Include -> Name attribute
                Attr includeName = doc.createAttribute("name");
                includeName.setValue(methodName);
                includeElement.setAttributeNode(includeName);
            }
        }
    }
}
