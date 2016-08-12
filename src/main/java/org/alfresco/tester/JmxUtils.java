package org.alfresco.tester;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.management.Attribute;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JmxUtils
{
    @Autowired
    protected TasProperties properties;

    private Log logger = LogFactory.getLog(JmxUtils.class);
    private final String JMX_URL_PATTERN = "service:jmx:rmi:///jndi/rmi://%s:%s/alfresco/jmxrmi";

    /**
     * Get Alfresco server property value
     * 
     * @param objectName
     * @param attributeName
     * @return
     */
    public Object getAlfrescoServerProperty(String objectName, String attributeName)
    {
        return getAlfrescoServerProperty(properties.getServer(), objectName, attributeName);
    }

    /**
     * @param hostURL
     * @param objectName
     * @param attributeName
     * @return
     */
    public Object getAlfrescoServerProperty(String hostURL, String objectName, String attributeName)
    {
        try
        {
            JMXConnector connector = makeJmxConnector(hostURL);
            MBeanServerConnection mBSC = connector.getMBeanServerConnection();
            ObjectName objectJmx = new ObjectName(objectName);
            Object result = mBSC.getAttribute(objectJmx, attributeName);
            connector.close();
            return result;
        }
        catch (InstanceNotFoundException ex)
        {
            return getAlfrescoServerProperty(getWasObjectName(objectName), attributeName);
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Set property value in Alfresco server
     * 
     * @param objectName
     * @param attributeName
     * @param attributeValue
     * @return
     */
    public Object setAlfrescoServerProperty(String objectName, String attributeName, Object attributeValue)
    {
        return setAlfrescoServerProperty(properties.getServer(), objectName, attributeName, attributeValue);
    }

    /**
     * @param hostURL
     * @param objectName
     * @param attributeName
     * @param attributeValue
     * @return
     */
    public Object setAlfrescoServerProperty(String hostURL, String objectName, String attributeName, Object attributeValue)
    {
        try
        {
            JMXConnector connector = makeJmxConnector(hostURL);
            MBeanServerConnection mBSC = connector.getMBeanServerConnection();
            ObjectName objectJmx = new ObjectName(objectName);

            mBSC.setAttribute(ObjectName.getInstance(objectName), new Attribute(attributeName, attributeValue));
            Object result = mBSC.getAttribute(objectJmx, attributeName);
            connector.close();
            return result;
        }
        catch (InstanceNotFoundException ex)
        {
            return setAlfrescoServerProperty(getWasObjectName(objectName), attributeName, attributeValue);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * @param objectName
     * @return String name from jmx with expected date in accordance with params
     */
    public String getWasObjectName(String objectName)
    {
        try
        {
            JMXConnector connector = makeJmxConnector();
            MBeanServerConnection mBSC = connector.getMBeanServerConnection();
            ObjectName wasObjectName = new ObjectName("*" + objectName + "*,cell=*,node=*,process=*");
            Set<?> set = mBSC.queryMBeans(wasObjectName, null);
            ObjectInstance oi = (ObjectInstance) set.toArray()[0];
            ObjectName oName = oi.getObjectName();
            String result = oName.toString();
            connector.close();
            return result;
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private JMXConnector makeJmxConnector() throws Exception
    {
        return makeJmxConnector(properties.getServer());
    }

    private JMXConnector makeJmxConnector(String hostUrl) throws Exception
    {
        try
        {
            String jmxUrlStr = String.format(JMX_URL_PATTERN, properties.getServer(), properties.getJmxPort());
            JMXServiceURL jmxUrl = new JMXServiceURL(jmxUrlStr);
            Map<String, String[]> env = new HashMap<>();
            env.put(JMXConnector.CREDENTIALS, new String[] { properties.getJmxUser(), properties.getJmxPassword() });

            JMXConnector connector = JMXConnectorFactory.connect(jmxUrl, env);
            return connector;
        }
        catch (Exception e)
        {
            logger.error(e);
            throw new Exception("Can't establish connection with JMX");
        }
    }

    /**
     * Use this to perform start/stop operation after changing server properties
     * 
     * @param objectName
     * @param operation
     */
    public void invokeAlfrescoServerProperty(String objectName, String operation)
    {
        invokeAlfrescoServerProperty(properties.getServer(), objectName, operation);
    }

    /**
     * @param hostURL
     * @param objectName
     * @param operation
     */
    public void invokeAlfrescoServerProperty(String hostURL, String objectName, String operation)
    {
        try
        {
            JMXConnector connector = makeJmxConnector(hostURL);
            MBeanServerConnection mBSC = connector.getMBeanServerConnection();
            ObjectName objectJmx = new ObjectName(objectName);
            mBSC.invoke(objectJmx, operation, new Object[] {}, new String[] {});
            connector.close();
        }
        catch (InstanceNotFoundException ex)
        {
            invokeAlfrescoServerProperty(getWasObjectName(objectName), operation);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }

}