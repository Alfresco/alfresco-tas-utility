package org.alfresco.utility;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.alfresco.utility.exception.ServerReachableAlfrescoIsNotRunningException;
import org.alfresco.utility.exception.ServerUnreachableException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServerHealth
{
    private Logger LOG = LogFactory.getLogger();
    private static final String ADMIN_SYSTEMSUMMARY_PAGE = "/alfresco/service/enterprise/admin";

    @Autowired
    protected TasProperties properties;

    public boolean isServerReachable() throws Exception
    {
        boolean reachable = false; 
        try {
            try (Socket soc = new Socket()) {
                soc.connect(new InetSocketAddress(properties.getServer(), properties.getPort()), 5000);
            }
            reachable = true;
        } catch (IOException ex) {
            LOG.info("Check Alfresco Test Server: {} is Reachable, found: {}", properties.getServer(), ex.getMessage());
            return false;
        }
        
        LOG.info("Check Alfresco Test Server: {} is Reachable, found: {}", properties.getServer(), reachable);
        return reachable;
    }

    public boolean isAlfrescoRunning() throws Exception
    {
        if (!isServerReachable())
            throw new ServerUnreachableException(properties);

        boolean isAlfrescoRunning = false;
        GetMethod get;
        String response;
        String alfrescoSummaryPage = properties.getFullServerUrl() + ADMIN_SYSTEMSUMMARY_PAGE;
        LOG.info("Check Alfresco Test Server: {} is Online based on Admin System Summary Page {}.", properties.getServer(), alfrescoSummaryPage);
        try
        {
            HttpClient client = new HttpClient();
            get = new GetMethod(alfrescoSummaryPage);
            get.setDoAuthentication(false);
            get.getParams().setSoTimeout(5000);
            client.executeMethod(get);
            response = IOUtils.toString(get.getResponseBodyAsStream());

            get.releaseConnection();
            isAlfrescoRunning = response.contains("alfresco");
        }
        catch (Exception ex)
        {
            LOG.error("Cannot GET {} page. Exception: {} ", alfrescoSummaryPage, ex.getMessage());
            isAlfrescoRunning = false;
        }

        return isAlfrescoRunning;
    }

    public void assertServerIsOnline() throws Exception
    {
        if (!isAlfrescoRunning())
            throw new ServerReachableAlfrescoIsNotRunningException(properties);
    }
}
