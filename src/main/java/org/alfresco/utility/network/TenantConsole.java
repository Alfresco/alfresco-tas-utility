package org.alfresco.utility.network;

import org.alfresco.utility.TasProperties;
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
    
    /**
     * Helper method that can be executed if TenantConsole is not initialized by @Autowired spring keyword
     */
    public boolean tenantExist(String scheme, String server, int port, String adminUserName, String adminPassword) throws Exception
    {
        properties = new TasProperties();
        properties.setScheme(scheme);
        properties.setServer(server);
        properties.setPort(port);
        properties.setAdminPassword(adminUserName);
        properties.setAdminUserName(adminUserName);
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
