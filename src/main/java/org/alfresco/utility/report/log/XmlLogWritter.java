package org.alfresco.utility.report.log;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.alfresco.utility.LogFactory;
import org.slf4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XmlLogWritter
{
    Logger LOG = LogFactory.getLogger();
    Properties logProperties = new Properties();
    private String logPath;
    private String fullPath;
    private final String dateFormat = "yyyy-MM-dd HH:mm:ss";

    public static List<String> testSteps = new ArrayList<String>();

    public XmlLogWritter()
    {
        InputStream defaultProp = getClass().getClassLoader().getResourceAsStream("default.properties");
        if (defaultProp != null)
        {
            try
            {
                logProperties.load(defaultProp);
                logPath = logProperties.getProperty("log.path");
                if (logPath == null)
                    logPath = ".";
            }
            catch (Exception e)
            {
                LOG.error("Cannot initialize Log Management Settings from default.properties file");
            }
        }
    }

    public void generateXmlFile(ITestContext context)
    {
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
            rootElement.appendChild(start);

            Element end = doc.createElement("end");
            rootElement.appendChild(end);

            Element duration = doc.createElement("duration");
            rootElement.appendChild(duration);

            Element total = doc.createElement("total");
            rootElement.appendChild(total);

            Element passed = doc.createElement("passed");
            rootElement.appendChild(passed);

            Element failed = doc.createElement("failed");
            rootElement.appendChild(failed);

            Element skipped = doc.createElement("skipped");
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
        }
        catch (Exception e)
        {
            LOG.error("Cannot create the xml file log. Error: {}", e.getMessage());
        }
    }

    public void setFinish(ITestContext context)
    {
        Document doc = getLogFile(fullPath);

        Node startTime = doc.getElementsByTagName("start").item(0);
        startTime.setTextContent(new SimpleDateFormat(dateFormat).format(context.getStartDate()));
        Node endTime = doc.getElementsByTagName("end").item(0);
        endTime.setTextContent(new SimpleDateFormat(dateFormat).format(context.getEndDate()));

        Node duration = doc.getElementsByTagName("duration").item(0);
        duration.setTextContent(getDuration(context.getEndDate().getTime(), context.getStartDate().getTime()));

        int passed = context.getPassedTests().size();
        int failed = context.getFailedTests().size();
        int skipped = context.getSkippedTests().size();

        Node totalTests = doc.getElementsByTagName("total").item(0);
        totalTests.setTextContent(Integer.toString(passed + failed + skipped));

        Node passedNode = doc.getElementsByTagName("passed").item(0);
        passedNode.setTextContent(Integer.toString(passed));

        Node failedNode = doc.getElementsByTagName("failed").item(0);
        failedNode.setTextContent(Integer.toString(failed));

        Node skippedNode = doc.getElementsByTagName("skipped").item(0);
        skippedNode.setTextContent(Integer.toString(skipped));
        updateLog(doc);
    }

    public void addTestExecution(ITestResult result, List<String> testSteps)
    {
        Document doc = getLogFile(fullPath);
        Node tests = doc.getElementsByTagName("tests").item(0);
        Node test = doc.createElement("test");
        tests.appendChild(test);

        Node name = doc.createElement("name");
        name.appendChild(doc.createTextNode(result.getMethod().getMethodName()));
        test.appendChild(name);

        Node nodeStatus = doc.createElement("status");
        nodeStatus.appendChild(doc.createTextNode(setStatus(result)));
        test.appendChild(nodeStatus);

        Node start = doc.createElement("start");
        long startTime = result.getStartMillis();
        start.appendChild(doc.createTextNode(new SimpleDateFormat(dateFormat).format(startTime)));
        test.appendChild(start);

        Node end = doc.createElement("end");
        long endTime = result.getEndMillis();
        end.appendChild(doc.createTextNode(new SimpleDateFormat(dateFormat).format(endTime)));
        test.appendChild(end);

        Node duration = doc.createElement("duration");
        duration.appendChild(doc.createTextNode(getDuration(endTime, startTime)));
        test.appendChild(duration);

        Node steps = doc.createElement("steps");
        test.appendChild(steps);
        Node stepNode = null;
        for (String step : testSteps)
        {
            stepNode = doc.createElement("step");
            stepNode.appendChild(doc.createTextNode(step));
            steps.appendChild(stepNode);
        }
        if (!result.isSuccess())
        {
            if (result.getThrowable() != null)
            {
                if (result.getThrowable().getStackTrace() != null)
                {
                    StringWriter sw = new StringWriter();
                    result.getThrowable().printStackTrace(new PrintWriter(sw));
                    Node error = doc.createElement("error");
                    error.appendChild(doc.createTextNode(sw.toString()));
                    test.appendChild(error);
                }
            }
        }
        updateLog(doc);
    }

    private String setStatus(ITestResult result)
    {
        String status = "";
        switch (result.getStatus())
        {
            case ITestResult.SUCCESS:
                status = "PASSED";
                break;
            case ITestResult.FAILURE:
                status = "FAILED";
                break;
            case ITestResult.SKIP:
                status = "SKIPPED";
                break;
            case ITestResult.SUCCESS_PERCENTAGE_FAILURE:
                status = "PASSED";
                break;
            default:
                break;
        }
        return status;
    }

    private void updateLog(Document document)
    {
        try
        {
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult streamResult = new StreamResult(fullPath);
            transformer.transform(source, streamResult);
        }
        catch (Exception e)
        {
            LOG.error("Cannot update the xml file log. Error: {}", e.getMessage());
        }
    }

    private Document getLogFile(String path)
    {
        Document doc = null;
        try
        {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = docFactory.newDocumentBuilder();
            doc = db.parse(fullPath);
        }
        catch (Exception e)
        {
            LOG.error("Unable to parse xml file. Error: {}", e.getMessage());
        }
        return doc;
    }

    private String getDuration(long endTime, long startTime)
    {
        return new SimpleDateFormat("mm:ss:SSS").format(new Date(endTime - startTime));
    }
}
