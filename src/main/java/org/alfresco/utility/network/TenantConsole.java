package org.alfresco.utility.network;

import org.alfresco.utility.model.UserModel;
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
    
    
    public boolean createTenant(String tenant, String password) throws Exception
    {
        return execute(new BasicNameValuePair("tenant-cmd", String.format("create %s %s", tenant, password))).toString().contains("created");
    }
    
    
    public UserModel createRandomTenant() throws Exception
    {
    	UserModel adminTenant = UserModel.getAdminTenantUser();
    	createTenant(adminTenant.getDomain(), "password");
    	return 	adminTenant;
    }
}
