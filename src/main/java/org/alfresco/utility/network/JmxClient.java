package org.alfresco.utility.network;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.alfresco.utility.LogFactory;
import org.alfresco.utility.TasProperties;
import org.alfresco.utility.exception.JmxException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Handles JMX calls to server
 * use {@link JmxBuilder} to define this service
 */
@Service
public class JmxClient implements Jmx
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
    @Override
    public Object readProperty(String objectName, String attributeName) throws Exception
    {
        JMXConnector connector = createJmxConnection();
        MBeanServerConnection mBSC = connector.getMBeanServerConnection();
        ObjectName objectJmx = new ObjectName(objectName);

        Object value = mBSC.getAttribute(objectJmx, attributeName);
        LOG.info("JMX Object [{}] with attribute [{}] has value [{}]", objectJmx, attributeName, value.toString());
        return value;
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
    @Override
    public Object writeProperty(String objectName, String attributeName, String attributeValue) throws Exception
    {
        JMXConnector connector = createJmxConnection();
        MBeanServerConnection mBSC = connector.getMBeanServerConnection();
        ObjectName objectJmx = new ObjectName(objectName);

        mBSC.setAttribute(ObjectName.getInstance(objectName), new Attribute(attributeName, attributeValue));
        LOG.info("Updating objectName {}.{} with value {} via JmxClient", objectName, attributeName, attributeValue);
        
        refreshServerProperty(objectName, JmxPropertyOperation.stop);
        refreshServerProperty(objectName, JmxPropertyOperation.start);
        return mBSC.getAttribute(objectJmx, attributeName);
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

        return oName.toString();
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

    @Override
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
            LOG.error("Cannot establish JMX Connection: {}", e.getMessage());
            e.printStackTrace();
        }

        return isAlive;
    }

    /**
     * Use this to invoke operation methodName on mBean objectName
     * Example of usage: executeJMXMethod("Alfresco:Name=DatabaseInformation,Tool=SchemaValidator”, "validateSchema");
     * @param objectName
     * @param methodName
     * @param pArgs
     * @return
     * @throws Exception
     */
    @Override
    public Object executeJMXMethod(String objectName, String methodName, Object ... pArgs) throws Exception
    {
        JMXConnector connector = getJmxConnection();
        MBeanServerConnection mBSC = connector.getMBeanServerConnection();
        ObjectName objectJmx = new ObjectName(objectName);

        LOG.info("Executing methodName {} on objectName {}  via JmxClient", methodName, objectName);
        return mBSC.invoke(objectJmx, methodName, pArgs, new String[]{});
    }

    /**
     * Use this to invoke operation methodName with specified signature on mBean objectName
     * Example of usage: executeJMXMethod("Alfresco:Name=DatabaseInformation,Tool=SchemaExport", "dumpSchemaToXML",  new String[]{"java.lang.String"}, "alf_node_, alf_acl_");
     * @param objectName
     * @param methodName
     * @param signature
     * @param pArgs
     * @return
     * @throws Exception
     */
    public Object executeJMXMethod(String objectName, String methodName, String signature[], Object ... pArgs) throws Exception
    {
        JMXConnector connector = getJmxConnection();
        MBeanServerConnection mBSC = connector.getMBeanServerConnection();
        ObjectName objectJmx = new ObjectName(objectName);

        LOG.info("Executing methodName {} with signature {} on objectName {}  via JmxClient", methodName, signature, objectName);
        return mBSC.invoke(objectJmx, methodName, pArgs, signature);
    }

}