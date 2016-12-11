package org.alfresco.utility.network;

import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;

/**
 * @author Paul Brodner
 */
@Service
public class WorkflowConsole extends HttpConsoleOperation
{
    public WorkflowConsole()
    {
        super("/alfresco/s/admin/admin-workflowconsole");
    }

    public String user() throws Exception
    {
        return execute(new BasicNameValuePair("workflow-cmd", "user")).toString();
    }
}
