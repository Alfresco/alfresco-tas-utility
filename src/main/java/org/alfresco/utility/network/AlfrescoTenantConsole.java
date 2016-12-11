package org.alfresco.utility.network;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

@Service
public class AlfrescoTenantConsole extends ServerHttpOperation
{
    public AlfrescoTenantConsole()
    {
        super("/alfresco/s/admin/admin-tenantconsole");
    }

    public void tenantsExists() throws Exception
    {               
        HttpPost httpPost = new HttpPost(getAlfrescoOperationPath());
        httpPost.addHeader("Authorization", getAdminBasicAuthentication());
        
        
        List <NameValuePair> nvps = new ArrayList <NameValuePair>();
        nvps.add(new BasicNameValuePair("tenant-cmd", "show tenants"));        
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        
        CloseableHttpResponse response2 = getHttpClient().execute(httpPost);
        
        try {
            System.out.println(response2.getStatusLine());
            HttpEntity entity2 = response2.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            String postPage = EntityUtils.toString(entity2);
        } finally {
            response2.close();
        }
        
        
        HttpGet httpGet = new HttpGet(getAlfrescoOperationPath());
        httpGet.addHeader("Authorization", getAdminBasicAuthentication());
        httpGet.addHeader("Content-Type", "text/html;charset=UTF-8");
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.98 Safari/537.36");
        
        
        
        HttpResponse r  = getHttpClient().execute(httpGet);
        
        EntityUtils.toString(r.getEntity());
        
        CloseableHttpResponse response1 = getHttpClient().execute(httpGet);
        
        try {
            System.out.println(response1.getStatusLine());
            
            
            HttpEntity entity1 = response1.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed

            entity1.isChunked();
            entity1.isStreaming();
            
            entity1.isRepeatable();
            
            
            
            
            String fullPage = EntityUtils.toString(entity1);
            EntityUtils.consume(entity1);
            Document doc = Jsoup.parse(fullPage.toString());
            
            Element fullSection = doc.select("div.column-full.section").first();
            System.out.println(fullSection);
            
        } finally {
            response1.close();
        }
        
    }
    
  
        
}
