package org.alfresco.utility;

import java.net.InetAddress;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testng.Assert;

@Service
public class ServerHealth
{
    private Logger LOG = LogFactory.getLogger();

    @Autowired
    protected TasProperties properties;

    public boolean isServerReachable() throws Exception
    {
        InetAddress byName = InetAddress.getByName(properties.getServer());
        boolean reachable = byName.isReachable(1000);

        LOG.info("Check Health Status of the Test Server: {}, found StatusOnline: {}", properties.getServer(), reachable);
        return reachable;
    }

    public void assertIfServerOnline() throws Exception
    {
        Assert.assertTrue(isServerReachable(), String.format("Server {%s} is online", properties.getServer()));
        
    }
}
