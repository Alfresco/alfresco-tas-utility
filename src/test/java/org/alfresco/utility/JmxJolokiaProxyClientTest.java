package org.alfresco.utility;

import org.alfresco.utility.network.JmxBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration("classpath:alfresco-tester-context.xml")
public class JmxJolokiaProxyClientTest extends AbstractTestNGSpringContextTests
{

    @Autowired
    JmxBuilder jmxBuilder;

    @Test
    public void executeRequest() throws Exception
    {
        System.out.println(jmxBuilder.getJmxClient().readProperty("Alfresco:Name=FileServerConfig", "CIFSServerName"));
    }
}
