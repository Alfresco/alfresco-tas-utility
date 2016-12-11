package org.alfresco.utility.network;

import org.alfresco.utility.TasProperties;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ServerHttpOperation
{
    protected String relativePath;    
    private CloseableHttpClient httpclient;

    public CloseableHttpClient getHttpClient()
    {
        return httpclient;
    }
    
    @Autowired
    protected TasProperties properties;

    public ServerHttpOperation(String relativePath)
    {
        this.relativePath = relativePath;
        this.httpclient = HttpClients.createDefault();
    }

    protected String getAlfrescoOperationPath()
    {
        return properties.getFullServerUrl() + relativePath;
    }

    /**
     * @return Basic Authentication 64 bit encoded of admin user (defined in *.properties file)
     */
    protected String getAdminBasicAuthentication()
    {
        String unhashedString = String.format("%s:%s", properties.getAdminUser(), properties.getAdminPassword());
        return "Basic " + Base64.encodeBase64String(unhashedString.getBytes());
    }
}
