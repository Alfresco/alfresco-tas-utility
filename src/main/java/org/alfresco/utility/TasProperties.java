package org.alfresco.utility;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

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

    @Value("${admin.user:admin}")
    private String adminUserName;

    @Value("${admin.password:admin}")
    private String adminPassword;

    @Value("${alfresco.scheme:http}")
    private String scheme;

    @Value("${alfresco.server:localhost}")
    private String server;

    @Value("${alfresco.port:8070}")
    private int port;

    @Value("${jmx.user:controlRole}")
    private String jmxUser;

    @Value("${jmx.password:change_asap}")
    private String jmxPassword;

    @Value("${jmx.port:50500}")
    private String jmxPort;

    @Value("${jmx.useJolokiaAgent:true}")
    private Boolean useJolokiaJmxAgent;

    @Value("${db.url:localhost}")
    private String dbUrl;

    @Value("${db.username:alfresco}")
    private String dbUsername;

    @Value("${db.password:alfresco}")
    private String dbPassword;
    
    @Value("${serverHealth.showTenants:true}")
    private Boolean showTenantsOnServerHealth;

    @Value("${browser.name:Firefox}")
    private String browserName;

    @Value("${browser.version:41}")
    private String browserVersion;

    @Value("${browser.implicitWait:10}")
    private long implicitWait;

    @Value("${browser.explicitWait:30}")
    private long explicitWait;
    
    @Value("${grid.url:not-set}")
    private String gridUrl;

    @Value("${grid.enabled:false}")
    private boolean gridEnabled;

    @Value("${screenshots.dir:screenshots}")
    private File screenshotsDir;

    @Value("${env.platform:WINDOWS}")
    private String envPlatformName;

    @Value("${share.url:http://localhost}")
    private URL shareUrl;

    @Value("${auth.context.factory:com.sun.jndi.ldap.LdapCtxFactory}")
    private String authContextFactory;

    @Value("${auth.security.authentication:simple}")
    private String securityAuth;

    @Value("${oracle.url:ldap://172.29.100.111:2389}")
    private String oracleURL;

    @Value("${oracle.security.principal:cn=Directory Manager}")
    private String oracleSecurityPrincipal;

    @Value("${oracle.security.credentials:directory}")
    private String oracleSecurityCredentials;

    @Value("${ldap.url:ldap://172.29.100.119:389}")
    private String ldapURL;

    @Value("${ldap.security.principal:CN=Administrator,CN=Users,DC=alfness,DC=com}")
    private String ldapSecurityPrincipal;

    @Value("${ldap.security.credentials:Alf1234}")
    private String ldapSecurityCredentials;

    @Value("${oldap.url:ldap://172.29.100.226:389}")
    private String oldapURL;

    @Value("${oldap.security.principal:CN=admin,DC=alfness,DC=com}")
    private String oldapSecurityPrincipal;

    @Value("${oldap.security.credentials:Alf1234}")
    private String oldapSecurityCredentials;

    @Value("${ntlm.host:172.29.100.126}")
    private String ntlmHost;

    @Value("${ntlm.security.principal:alfntlm\\Administrator}")
    private String ntlmSecurityPrincipal;

    @Value("${ntlm.security.credentials:Alf1234}")
    private String ntlmSecurityCredentials;

    public Boolean showTenantsOnServerHealth()
    {
        return showTenantsOnServerHealth;
    }

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

    public String getBrowserName()
    {
        return browserName;
    }

    public void setBrowserName(String browserName)
    {
        this.browserName = browserName;
    }

    public String getBrowserVersion()
    {
        return browserVersion;
    }

    public void setBrowserVersion(String browserVersion)
    {
        this.browserVersion = browserVersion;
    }

    public long getImplicitWait()
    {
        return implicitWait;
    }

    public void setImplicitWait(long implicitWait)
    {
        this.implicitWait = implicitWait;
    }
    
    public long getExplicitWait()
    {
        return explicitWait;
    }

    public void setExplicitWait(long explicitWait)
    {
        this.explicitWait = explicitWait;
    }

    public URL getGridUrl() throws MalformedURLException
    {
        return new URL(gridUrl);
    }

    public void setGridUrl(String gridUrl)
    {
        this.gridUrl = gridUrl;
    }

    public boolean isGridEnabled()
    {
        return gridEnabled;
    }

    public void setGridEnabled(boolean gridEnabled)
    {
        this.gridEnabled = gridEnabled;
    }

    public File getScreenshotsDir()
    {
        return screenshotsDir;
    }

    public void setScreenshotsDir(String screenshotsDir)
    {
        File f = Paths.get(screenshotsDir).toFile();
        if (f.isFile() && !f.exists())
        {
            f.getParentFile().mkdirs();
        }
        else if (!f.exists())
        {
            f.mkdirs();
        }
        this.screenshotsDir = f;
    }

    public String getEnvPlatformName()
    {
        return envPlatformName;
    }

    public void setEnvPlatformName(String envPlatformName)
    {
        this.envPlatformName = envPlatformName;
    }

    public URL getShareUrl()
    {
        return shareUrl;
    }

    public void setShareUrl(URL shareUrl)
    {
        this.shareUrl = shareUrl;
    }

    public String getAuthContextFactory()
    {
        return authContextFactory;
    }

    public void setAuthContextFactory(String authContextFactory)
    {
        this.authContextFactory = authContextFactory;
    }

    public String getSecurityAuth()
    {
        return securityAuth;
    }

    public void setSecurityAuth(String securityAuth)
    {
        this.securityAuth = securityAuth;
    }

    public String getOracleURL()
    {
        return oracleURL;
    }

    public void setOracleURL(String oracleURL)
    {
        this.oracleURL = oracleURL;
    }

    public String getOracleSecurityPrincipal()
    {
        return oracleSecurityPrincipal;
    }

    public void setOracleSecurityPrincipal(String oracleSecurityPrincipal)
    {
        this.oracleSecurityPrincipal = oracleSecurityPrincipal;
    }

    public String getOracleSecurityCredentials()
    {
        return oracleSecurityCredentials;
    }

    public void setOracleSecurityCredentials(String oracleSecurityCredentials)
    {
        this.oracleSecurityCredentials = oracleSecurityCredentials;
    }

    public String getLdapURL()
    {
        return ldapURL;
    }

    public void setLdapURL(String ldapURL)
    {
        this.ldapURL = ldapURL;
    }

    public String getLdapSecurityPrincipal()
    {
        return ldapSecurityPrincipal;
    }

    public void setLdapSecurityPrincipal(String ldapSecurityPrincipal)
    {
        this.ldapSecurityPrincipal = ldapSecurityPrincipal;
    }

    public String getLdapSecurityCredentials()
    {
        return ldapSecurityCredentials;
    }

    public void setLdapSecurityCredentials(String ldapSecurityCredentials)
    {
        this.ldapSecurityCredentials = ldapSecurityCredentials;
    }

    public String getOLdapURL()
    {
        return oldapURL;
    }

    public void setOLdapURL(String oldapURL)
    {
        this.oldapURL = oldapURL;
    }

    public String getOLdapSecurityPrincipal()
    {
        return oldapSecurityPrincipal;
    }

    public void setOLdapSecurityPrincipal(String oldapSecurityPrincipal)
    {
        this.oldapSecurityPrincipal = oldapSecurityPrincipal;
    }

    public String getOLdapSecurityCredentials()
    {
        return oldapSecurityCredentials;
    }

    public void setOLdapSecurityCredentials(String oldapSecurityCredentials)
    {
        this.oldapSecurityCredentials = oldapSecurityCredentials;
    }

    public String getNtlmHost()
    {
        return ntlmHost;
    }

    public void setNtlmHost(String ntlmHost)
    {
        this.ntlmHost = ntlmHost;
    }

    public String getNtlmSecurityPrincipal()
    {
        return ntlmSecurityPrincipal;
    }

    public void setNtlmSecurityPrincipal(String ntlmSecurityPrincipal)
    {
        this.ntlmSecurityPrincipal = ntlmSecurityPrincipal;
    }

    public String getNtlmSecurityCredentials()
    {
        return ntlmSecurityCredentials;
    }

    public void setNtlmSecurityCredentials(String ntlmSecurityCredentials)
    {
        this.ntlmSecurityCredentials = ntlmSecurityCredentials;
    }

}