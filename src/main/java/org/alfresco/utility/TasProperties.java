package org.alfresco.utility;

import org.apache.chemistry.opencmis.commons.impl.UrlBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

/**
 * Generic Properties class that will load and override properties based on
 * environment defined For "test" environment just define {code}
 * System.setProperty("environment", "test"); {code} or run your test passing
 * -Denvironment=test
 * 
 * @author Paul.Brodner
 */
@Configuration
@PropertySource("classpath:default.properties")
@PropertySource(value = "classpath:${environment}.properties", ignoreResourceNotFound = true)
public class TasProperties
{
    @Autowired
    Environment env;

    @Value("${admin.user}")
    private String adminUserName;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${alfresco.scheme}")
    private String scheme;

    @Value("${alfresco.server}")
    private String server;

    @Value("${alfresco.port}")
    private int port;

    @Value("${jmx.user:controlRole}")
    private String jmxUser;

    @Value("${jmx.password:change_asap}")
    private String jmxPassword;

    @Value("${jmx.port:50500}")
    private String jmxPort;

    @Value("${jmx.useJolokiaAgent:true}")
    private Boolean useJolokiaJmxAgent;

    @Value("${db.url}")
    private String dbUrl;

    @Value("${db.username}")
    private String dbUsername;

    @Value("${db.password}")
    private String dbPassword;

    /**
     * # in containers we cannot access directly JMX, so we will use {@link http://jolokia.org} agent
     * # disabling this we will use direct JMX calls to server
     */
    public boolean useJolokiaJmxAgent()
    {
        return useJolokiaJmxAgent;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer()
    {
        return new PropertySourcesPlaceholderConfigurer();
    }

    public Environment getEnv()
    {
        return env;
    }

    public String getAdminUser()
    {
        return adminUserName;
    }

    public void setAdminUserName(String adminUserName)
    {
        this.adminUserName = adminUserName;
    }

    public String getAdminPassword()
    {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword)
    {
        this.adminPassword = adminPassword;
    }

    public String getScheme()
    {
        return scheme;
    }

    public void setScheme(String scheme)
    {
        this.scheme = scheme;
    }

    public String getServer()
    {
        return server;
    }

    public void setServer(String server)
    {
        this.server = server;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public String getJmxUser()
    {
        return jmxUser;
    }

    public void setJmxUser(String jmxUser)
    {
        this.jmxUser = jmxUser;
    }

    public String getJmxPassword()
    {
        return jmxPassword;
    }

    public void setJmxPassword(String jmxPassword)
    {
        this.jmxPassword = jmxPassword;
    }

    public String getJmxPort()
    {
        return jmxPort;
    }

    public void setJmxPort(String jmxPort)
    {
        this.jmxPort = jmxPort;
    }

    public String getJmxUrl()
    {
        return String.format("service:jmx:rmi:///jndi/rmi://%s:%s/alfresco/jmxrmi", getServer(), getJmxPort());
    }

    /**
     * @return host: <schema>://<server>:<port>
     */
    public String getFullServerUrl()
    {
        return new UrlBuilder(getScheme(), getServer(), getPort(), "").toString();
    }

    /**
     * @return host: <schema>://<server>
     */
    public String getTestServerUrl()
    {
        return String.format("%s://%s", getScheme(), getServer());
    }

    public String getDbUrl()
    {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl)
    {
        this.dbUrl = dbUrl;
    }

    public String getDbUsername()
    {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername)
    {
        this.dbUsername = dbUsername;
    }

    public String getDbPassword()
    {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword)
    {
        this.dbPassword = dbPassword;
    }

}