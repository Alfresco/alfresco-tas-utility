package org.alfresco.utility.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.alfresco.utility.LogFactory;
import org.alfresco.utility.TasProperties;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Paul Brodner
 */
public abstract class HttpConsoleOperation
{
    @Autowired
    protected TasProperties properties;
    protected static Logger LOG = LogFactory.getLogger();

    protected String relativePath;
    private CloseableHttpClient httpclient;

    /**
     * @return HTTP Client used in POST calls
     */
    public CloseableHttpClient getHttpClient()
    {
        return httpclient;
    }

    /**
     * Pass the relative path of the "console" page, take a look at the implementation of {@link TenantConsole} class
     * 
     * @param relativePath
     */
    public HttpConsoleOperation(String relativePath)
    {
        this.relativePath = relativePath;
        this.httpclient = HttpClients.createDefault();
    }

    /**
     * @return the full path (server & relative path) of console page
     */
    protected String getAlfrescoConsolePath()
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

    /**
     * This is the method that will perform the actual POST command on alfresco console
     * 
     * @return
     * @throws Exception
     */
    public Element execute(BasicNameValuePair command) throws Exception
    {
        HttpPost httpPost = new HttpPost(getAlfrescoConsolePath());
        httpPost.addHeader("Authorization", getAdminBasicAuthentication());

        List<NameValuePair> commands = new ArrayList<NameValuePair>();
        commands.add(command);
        httpPost.setEntity(new UrlEncodedFormEntity(commands));

        CloseableHttpResponse postResponse = getHttpClient().execute(httpPost);
        try
        {
            LOG.info("Executing command: {} -> Response: {}", String.valueOf(commands), postResponse.getStatusLine());
            HttpEntity postEntity = postResponse.getEntity();
            EntityUtils.consume(postEntity);
        }
        finally
        {
            postResponse.close();
        }

        return parsedResponse();
    }

    /**
     * Perform the get request on console page.
     * 
     * @return {@link Document} object with entire HTML page
     * @throws Exception
     */
    protected Document fullResponseDocument() throws Exception
    {
        URL url = new URL(getAlfrescoConsolePath());

        URLConnection uc = url.openConnection();
        uc.setRequestProperty("X-Requested-With", "TAS-Client");
        uc.setRequestProperty("Authorization", getAdminBasicAuthentication());

        String fullPage = "";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(uc.getInputStream(), "UTF-8")))
        {
            for (String line; (line = reader.readLine()) != null;)
            {
                fullPage += line;
            }
        }

        return Jsoup.parse(fullPage.toString());
    }

    /**
     * @return the response value parsed
     * @throws Exception
     */
    protected Element parsedResponse() throws Exception
    {
        Element last = fullResponseDocument().select("div.column-full").last();
        LOG.info("Response: {}", last.toString());
        return last;
    }
}
