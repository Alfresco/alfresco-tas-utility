package org.alfresco.utility;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.management.Attribute;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.alfresco.utility.exception.JmxException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JmxClient
{
    @Autowired
    protected TasProperties properties;

    static Logger LOG = LogFactory.getLogger();

    private JMXConnector jmxConnector;

    public enum JmxPropertyOperation
    {
        stop, start
    }

    /**
     * Get server property value
     * Example:
     * Alfresco:Name=FileServerConfig, CIFSServerEnabled
     * getServerProperty("Alfresco:Name=FileServerConfig", "CIFSServerEnabled")
     * 
     * @param objectName
     * @param attributeName
     * @return
     */
    public Object getServerProperty(String objectName, String attributeName) throws Exception
    {
        JMXConnector connector = createJmxConnection();
        MBeanServerConnection mBSC = connector.getMBeanServerConnection();
        ObjectName objectJmx = new ObjectName(objectName);
        
        Object value = mBSC.getAttribute(objectJmx, attributeName);
        LOG.info("JMX Object [{}] with attribute [{}] has value [{}]", objectJmx, attributeName, value.toString());
        return value ;
    }

    public void closeConnection() throws IOException
    {
        if (jmxConnector != null)
            jmxConnector.close();
    }

    /**
     * Set property value on server side, for example if you want to disable email server, you'll use the following parameters:
     * <br/>
     * setServerProperty("Alfresco:Type=Configuration,Category=email,id1=inbound", "email.server.enabled", "false")
     * 
     * @param objectName
     * @param attributeName
     * @param attributeValue
     * @return
     */
    public Object setServerProperty(String objectName, String attributeName, Object attributeValue) throws Exception
    {
        JMXConnector connector = createJmxConnection();
        MBeanServerConnection mBSC = connector.getMBeanServerConnection();
        ObjectName objectJmx = new ObjectName(objectName);

        mBSC.setAttribute(ObjectName.getInstance(objectName), new Attribute(attributeName, attributeValue));
        Object result = mBSC.getAttribute(objectJmx, attributeName);
        connector.close();

        return result;
    }

    /**
     * @param objectName
     * @return String name from JMX with expected date in accordance with params
     */
    public String getStringNameFromJmxObject(String objectName) throws Exception
    {
        JMXConnector connector = getJmxConnection();
        MBeanServerConnection mBSC = connector.getMBeanServerConnection();
        ObjectName wasObjectName = new ObjectName("*" + objectName + "*,cell=*,node=*,process=*");
        Set<?> set = mBSC.queryMBeans(wasObjectName, null);
        ObjectInstance oi = (ObjectInstance) set.toArray()[0];
        ObjectName oName = oi.getObjectName();
        String result = oName.toString();
        connector.close();

        return result;
    }

    /**
     * Get JMX connection
     * 
     * @return
     * @throws IOException
     * @throws JmxException
     */
    private JMXConnector getJmxConnection() throws IOException, JmxException
    {
        return createJmxConnection();
    }

    /**
     * Create JMX connection
     * 
     * @return
     * @throws JmxException
     * @throws IOException
     */
    private JMXConnector createJmxConnection() throws JmxException, IOException
    {
        if (jmxConnector == null)
        {
            JMXServiceURL jmxUrl = new JMXServiceURL(properties.getJmxUrl());
            Map<String, String[]> env = new HashMap<>();
            env.put(JMXConnector.CREDENTIALS, new String[] { properties.getJmxUser(), properties.getJmxPassword() });

            LOG.info("Create JMX Connection using [{}] with username [{}] and password [{}] ", properties.getJmxUrl(), properties.getJmxUser(),
                    properties.getJmxPassword());
            jmxConnector = JMXConnectorFactory.connect(jmxUrl, env);
        }
        return jmxConnector;
    }

    /**
     * Use this to perform start/stop operation after changing server properties
     * 
     * @param objectName
     * @param operation
     * @throws MalformedObjectNameException
     * @throws ReflectionException
     * @throws MBeanException
     * @throws InstanceNotFoundException
     */
    public void refreshServerProperty(String objectName, JmxPropertyOperation operation) throws Exception
    {
        JMXConnector connector = createJmxConnection();
        MBeanServerConnection mBSC = connector.getMBeanServerConnection();
        ObjectName objectJmx = new ObjectName(objectName);
        mBSC.invoke(objectJmx, operation.toString(), new Object[] {}, new String[] {});
    }

    public boolean isJMXEnabled()
    {
        boolean isAlive = false;
        try
        {
            createJmxConnection();
            isAlive = true;
        }
        catch (Exception e)
        {
            LOG.error("Cannot establish JMX Connection {}", e.getMessage());
            e.printStackTrace();
        }

        return isAlive;
    }

}