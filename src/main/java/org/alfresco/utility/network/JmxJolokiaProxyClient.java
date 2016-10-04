package org.alfresco.utility.network;

import java.io.IOException;

import org.alfresco.utility.LogFactory;
import org.alfresco.utility.TasProperties;
import org.alfresco.utility.exception.EnvironmentConfigurationException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.jolokia.client.J4pClient;
import org.jolokia.client.exception.J4pRemoteException;
import org.jolokia.client.request.J4pReadRequest;
import org.jolokia.client.request.J4pReadResponse;
import org.jolokia.client.request.J4pRequest;
import org.jolokia.client.request.J4pResponse;
import org.jolokia.client.request.J4pWriteRequest;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Jolokia is a JMX-HTTP bridge giving an alternative to JSR-160 connectors.
 * It is an agent based approach with support for many platforms.
 * In addition to basic JMX operations it enhances JMX remoting with unique features like bulk requests and fine grained security policies.
 * {@link https://jolokia.org}
 * In order to use this class you need to have this jolokia agent started on test machine
 * use {@link JmxBuilder} to define this service
 */
@Service
public class JmxJolokiaProxyClient implements Jmx
{
    private static Logger LOG = LogFactory.getLogger();
    @Autowired
    protected TasProperties properties;
    private J4pClient client;

    @Override
    public Object readProperty(String objectName, String attributeName) throws Exception
    {
        J4pReadRequest request = new J4pReadRequest(objectName, attributeName);
        J4pReadResponse response = (J4pReadResponse) executeRequest(request);

        return response.getValue().toString();
    }

    @Override
    public Object writeProperty(String objectName, String attributeName, String attributeValue) throws Exception
    {
        J4pWriteRequest requestW = new J4pWriteRequest(objectName, attributeName, attributeValue, "");
        J4pResponse<J4pWriteRequest> response = getClient().execute(requestW);
        LOG.info("Updating objectName {}.{} with value {} via JmxJolokia", objectName, attributeName, attributeValue);
        return response.getValue();
    }

    private J4pResponse<?> executeRequest(J4pRequest request) throws Exception
    {
        J4pResponse<?> response;
        try
        {
            response = getClient().execute(request);
        }
        catch (J4pRemoteException e)
        {
            throw new EnvironmentConfigurationException("It seems Jolokia agent was not installed on test environment or missconfigured. Error thrown: " + e.getMessage());
        }

        return response;
    }

    public J4pClient getClient()
    {
        if (client == null)
            client = new J4pClient(getJolokiaAgentServerUrlPath());
        return client;
    }

    public String getJolokiaAgentServerUrlPath()
    {
        return String.format("%s/jolokia", properties.getFullServerUrl());
    }

    @Override
    public boolean isJMXEnabled()
    {
        boolean isEnabled = false;
        HttpClient client = new HttpClient();
        GetMethod get = new GetMethod(getJolokiaAgentServerUrlPath());
        get.setDoAuthentication(false);
        get.getParams().setSoTimeout(5000);
        try
        {
            client.executeMethod(get);
            String response = IOUtils.toString(get.getResponseBodyAsStream());
            isEnabled = response.contains("agentId");
        }
        catch (HttpException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        get.releaseConnection();

        return isEnabled;
    }

}
