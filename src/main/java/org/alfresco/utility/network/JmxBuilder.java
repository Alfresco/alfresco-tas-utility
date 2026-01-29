package org.alfresco.utility.network;


import jakarta.annotation.PostConstruct;
import org.alfresco.utility.LogFactory;
import org.alfresco.utility.TasProperties;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class JmxBuilder
{
    static Logger LOG = LogFactory.getLogger();

    @Autowired
    private TasProperties tasProperties;

    @Autowired
    private JmxClient jmxClient;

    @Autowired
    private JmxJolokiaProxyClient jmxJolokiaProxyClient;

    private Jmx jmxClientInstance = null;

    @PostConstruct
    private void defineJmxClient()
    {
        if (tasProperties.useJolokiaJmxAgent())
        {
            LOG.info(
                    "Using Jolokia Agent for interacting with JMX on test server {}. Update your *.properties in order to disable this service (i.e. jmx.useJolokiaAgent=false).",
                    jmxJolokiaProxyClient.getJolokiaAgentServerUrlPath());
            jmxClientInstance = jmxJolokiaProxyClient;
        }
        else
        {
            LOG.info(
                    "Using JMX Client for interacting with JMX on test server. Update your *.properties in order to use Jolokia agent as service (i.e. jmx.useJolokiaAgent=true).");
            jmxClientInstance = jmxClient;
        }
    }

    public Jmx getJmxClient()
    {
        return jmxClientInstance;
    }

}
