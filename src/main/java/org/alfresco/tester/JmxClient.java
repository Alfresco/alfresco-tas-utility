package org.alfresco.tester;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.springframework.beans.factory.annotation.Autowired;
import org.alfresco.tester.exception.JmxException;
import org.springframework.stereotype.Service;

@Service
public class JmxClient
{
    @Autowired
    protected TasProperties properties;

    /**
     * Get server property value
     * 
     * @param objectName
     * @param attributeName
     * @return
     * @throws IOException
     * @throws ReflectionException
     * @throws MBeanException
     * @throws AttributeNotFoundException
     * @throws MalformedObjectNameException
     * @throws InstanceNotFoundException
     */
    public Object getServerProperty(String objectName, String attributeName) throws JmxException, IOException, AttributeNotFoundException,
            InstanceNotFoundException, MBeanException, ReflectionException, MalformedObjectNameException
    {
        JMXConnector connector = createJmxConnection();
        MBeanServerConnection mBSC = connector.getMBeanServerConnection();
        ObjectName objectJmx = new ObjectName(objectName);
        Object result = mBSC.getAttribute(objectJmx, attributeName);
        connector.close();

        return result;
    }

    /**
     * Set property value on server side
     * 
     * @param objectName
     * @param attributeName
     * @param attributeValue
     * @return
     * @throws IOException
     * @throws ReflectionException
     * @throws MBeanException
     * @throws InstanceNotFoundException
     * @throws AttributeNotFoundException
     * @throws NullPointerException
     * @throws MalformedObjectNameException
     * @throws InvalidAttributeValueException
     */
    public Object setServerProperty(String objectName, String attributeName, Object attributeValue)
            throws JmxException, IOException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException,
            InvalidAttributeValueException, MalformedObjectNameException, NullPointerException
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
     * Get string name of an object from JMX
     * 
     * @param objectName
     * @return String name from JMX with expected date in accordance with params
     * @throws JmxException
     * @throws IOException
     * @throws MalformedObjectNameException
     */
    public String getStringNameFromJmxObject(String objectName) throws JmxException, IOException, MalformedObjectNameException
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
        String jmxUrlStr = String.format(properties.getJmxUrl(), properties.getServer(), properties.getJmxPort());
        JMXServiceURL jmxUrl = new JMXServiceURL(jmxUrlStr);
        Map<String, String[]> env = new HashMap<>();
        env.put(JMXConnector.CREDENTIALS, new String[] { properties.getJmxUser(), properties.getJmxPassword() });
        JMXConnector connector = JMXConnectorFactory.connect(jmxUrl, env);

        return connector;
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
    public void invokeServerProperty(String objectName, String operation)
            throws JmxException, IOException, MalformedObjectNameException, InstanceNotFoundException, MBeanException, ReflectionException
    {
        JMXConnector connector = createJmxConnection();
        MBeanServerConnection mBSC = connector.getMBeanServerConnection();
        ObjectName objectJmx = new ObjectName(objectName);
        mBSC.invoke(objectJmx, operation, new Object[] {}, new String[] {});
        connector.close();
    }

}