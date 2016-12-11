package org.alfresco.utility.network;

import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;

@Service
public class TenantConsole extends HttpConsoleOperation
{
    public TenantConsole()
    {
        super("/alfresco/s/admin/admin-tenantconsole");
    }

    public boolean tenantExist() throws Exception
    {
        return execute(new BasicNameValuePair("tenant-cmd", "show tenants")).toString().contains("tenant");
    }
}
