package org.alfresco.utility.report.log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.alfresco.utility.LogFactory;
import org.alfresco.utility.Utility;
import org.slf4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class XmlLogWritter
{
    Logger LOG = LogFactory.getLogger();
    private boolean configurationError = true;
    Properties logProperties = new Properties();
    private String logPath;
    private String fullPath;
    
    public static List<String> testSteps = new ArrayList<String>();

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

            // suite element
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("suite");
            doc.appendChild(rootElement);

            // set attribute to suite element
            Attr suiteName = doc.createAttribute("name");
            suiteName.setValue(context.getCurrentXmlTest().getSuite().getName());
            rootElement.setAttributeNode(suiteName);

            // class element
            Element className = doc.createElement("class");
            rootElement.appendChild(className);

            // set attribute class name
            Attr classValue = doc.createAttribute("name");
            classValue.setValue(context.getCurrentXmlTest().getClasses().get(0).getName());
            className.setAttributeNode(classValue);

            // start time
            Element start = doc.createElement("start");
            start.appendChild(doc.createTextNode(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(context.getStartDate())));
            rootElement.appendChild(start);
            
            Element end = doc.createElement("end");
            end.appendChild(doc.createTextNode("0"));
            rootElement.appendChild(end);

            Element total = doc.createElement("total");
            total.appendChild(doc.createTextNode("0"));
            rootElement.appendChild(total);

            Element passed = doc.createElement("passed");
            passed.appendChild(doc.createTextNode("0"));
            rootElement.appendChild(passed);

            Element failed = doc.createElement("failed");
            failed.appendChild(doc.createTextNode("0"));
            rootElement.appendChild(failed);

            Element skipped = doc.createElement("skipped");
            skipped.appendChild(doc.createTextNode("0"));
            rootElement.appendChild(skipped);

            Element tests = doc.createElement("tests");
            rootElement.appendChild(tests);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            fullPath = logPath + File.separator + context.getCurrentXmlTest().getClasses().get(0).getName() + "-"
                    + new SimpleDateFormat("yyyy-MM-dd_HHmmss").format(context.getStartDate()) + ".xml";

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

    public void setFinish(ITestContext context)
    {
        try
        {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = docFactory.newDocumentBuilder();
            Document doc = db.parse(fullPath);

            Node endTime = doc.getElementsByTagName("end").item(0);
            endTime.setTextContent(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(context.getEndDate()));

            Node passed = doc.getElementsByTagName("passed").item(0);
            passed.setTextContent(Integer.toString(context.getPassedTests().size()));

            Node failed = doc.getElementsByTagName("failed").item(0);
            failed.setTextContent(Integer.toString(context.getFailedTests().size()));

            Node skipped = doc.getElementsByTagName("skipped").item(0);
            skipped.setTextContent(Integer.toString(context.getSkippedTests().size()));

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(fullPath);
            transformer.transform(source, result);
        }
        catch (ParserConfigurationException pce)
        {
            pce.printStackTrace();
        }
        catch (TransformerException tfe)
        {
            tfe.printStackTrace();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        catch (SAXException sae)
        {
            sae.printStackTrace();
        }
    }

    public void addTestExecution(ITestResult result, List<String> testSteps)
    {
        try
        {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = docFactory.newDocumentBuilder();
            Document doc = db.parse(fullPath);
            Node tests = doc.getElementsByTagName("tests").item(0);
            
            Node test = doc.createElement("test");
            tests.appendChild(test);
            
            Node name = doc.createElement("name");
            name.appendChild(doc.createTextNode(result.getMethod().getMethodName()));
            test.appendChild(name);
            
            Node start = doc.createElement("start");
            start.appendChild(doc.createTextNode(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(result.getStartMillis())));
            test.appendChild(start);
            
            Node steps = doc.createElement("steps");
            test.appendChild(steps);
            
            for(String step : testSteps)
            {
                Node stepNode = doc.createElement("step");
                stepNode.appendChild(doc.createTextNode(step));
                steps.appendChild(stepNode);
            }
           
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(fullPath);
            transformer.transform(source, streamResult);
        }
        catch (ParserConfigurationException pce)
        {
            pce.printStackTrace();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        catch (SAXException sae)
        {
            sae.printStackTrace();
        }
        catch (TransformerConfigurationException e)
        {
            e.printStackTrace();
        }
        catch (TransformerException e)
        {
            e.printStackTrace();
        }
    }
}
