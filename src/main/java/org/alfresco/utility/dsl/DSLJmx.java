package org.alfresco.utility.dsl;

import static org.alfresco.utility.report.log.Step.STEP;

import org.alfresco.utility.network.Jmx;
import org.alfresco.utility.network.JmxClient;
import org.alfresco.utility.network.JmxJolokiaProxyClient;

/**
 * DSL for interacting with JMX (using direct JMX call see {@link JmxClient} or {@link JmxJolokiaProxyClient}
 */
public class DSLJmx
{
    protected Jmx jmx;
    
    public DSLJmx(Jmx jmx)
    {
        this.jmx = jmx;
    }
    
    /**
     * Alfresco:Type=Configuration,Category=email,id1=inbound
     * email.server.auth.enabled
     * Boolean.FALSE.toString()s
     * 
     * @param jmxPropertyName
     * @param jmxPropertyNewValue
     * @throws Exception
     */
    
    /**
     * Update property value on server side through jmx connection, for example if you want to disable email server, you'll use the following parameters:
     * <br/>
     * updatePropertyWithValue("Alfresco:Type=Configuration,Category=email,id1=inbound", "email.server.enabled", "false")
     * 
     * @param jmxObjectName
     * @param jmxAttributeName
     * @param jmxAttributeNewValue
     * @return
     */
    public void updatePropertyWithValue(String jmxObjectName, String jmxAttributeName, String jmxAttributeNewValue) throws Exception
    {
        STEP(String.format("JMX attribute: Update JMX attribute %s", jmxAttributeName));
        jmx.writeProperty(jmxObjectName, jmxAttributeName, jmxAttributeNewValue);
    }

    /**
     * Get server property value through jmx connection
     * Example:
     * Alfresco > Configuration > email > inbound , email.server.enabled
     * getPropertyValue("Alfresco:Type=Configuration,Category=email,id1=inbound", "email.server.enabled")
     * 
     * @param jmxObjectName
     * @param jmxAttributeName
     * @return
     */
    public String getPropertyValue(String jmxObjectName, String jmxAttributeName) throws Exception
    {
        STEP(String.format("Get JMX attribute value of ", jmxAttributeName));
        return jmx.readProperty(jmxObjectName, jmxAttributeName).toString();
    }
}
