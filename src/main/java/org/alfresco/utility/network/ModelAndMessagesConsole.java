package org.alfresco.utility.network;

import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;

/**
 * @author Paul Brodner
 */
@Service
public class ModelAndMessagesConsole extends HttpConsoleOperation
{
    public ModelAndMessagesConsole()
    {
        super("/alfresco/s/admin/admin-repoconsole");
    }

    public String showModels() throws Exception
    {
        return execute(new BasicNameValuePair("repo-cmd", "show models")).toString();
    }

    public boolean modelsDeployed() throws Exception
    {
        return showModels().contains("No additional models have been deployed to the Alfresco Repository");
    }
}
