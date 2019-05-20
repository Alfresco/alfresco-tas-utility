package org.alfresco.utility;

import org.keycloak.representations.adapters.config.AdapterConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;
import java.util.TreeMap;

/**
 * Properties specific to Alfresco Identity Service.
 */
@Configuration
@PropertySource("classpath:default.properties")
@PropertySource(value = "classpath:${environment}.properties", ignoreResourceNotFound = true)
public class TasAisProperties
{

    AdapterConfig adapterConfig = null;

    @Value("${identity-service.realm:alfresco}")
    String realm;

    @Value("${identity-service.realm-public-key:#{null}}")
    String realmKey;

    @Value("${identity-service.auth-server-url:#{null}}")
    String authServerUrl;

    @Value("${identity-service.ssl-required:external}")
    String sslRequired;

    @Value("${identity-service.confidential-port:0}")
    int confidentialPort;

    @Value("${identity-service.resource:alfresco}")
    String resource;

    @Value("${identity-service.use-resource-role-mappings:false}")
    boolean useResourceRoleMappings;

    @Value("${identity-service.enable-cors:false}")
    boolean enableCors;

    @Value("${identity-service.cors-max-age:-1}")
    int corsMaxAge;

    @Value("${identity-service.cors-allowed-headers:#{null}}")
    String corsAllowedHeaders;

    @Value("${identity-service.cors-allowed-methods:#{null}}")
    String corsAllowedMethods;

    @Value("${identity-service.cors-exposed-headers:#{null}}")
    String corsExposedHeaders;

    @Value("${identity-service.expose-token:false}")
    boolean exposeToken;

    @Value("${identity-service.bearer-only:false}")
    boolean bearerOnly;

    @Value("${identity-service.autodetect-bearer-only:false}")
    boolean autodetectBearerOnly;

    @Value("${identity-service.enable-basic-auth:false}")
    boolean enableBasicAuth;

    @Value("${identity-service.public-client:false}")
    boolean publicClient;

    @Value("${identity-service.allow-any-hostname:false}")
    boolean allowAnyHostname;

    @Value("${identity-service.disable-trust-manager:false}")
    boolean disableTrustManager;

    @Value("${identity-service.truststore:#{null}}")
    String truststore;

    @Value("${identity-service.truststore-password:#{null}}")
    String truststorePassword;

    @Value("${identity-service.client-keystore:#{null}}")
    String clientKeystore;

    @Value("${identity-service.client-keystore-password:#{null}}")
    String clientKeystorePassword;

    @Value("${identity-service.client-key-password:#{null}}")
    String clientKeyPassword;

    @Value("${identity-service.connection-pool-size:20}")
    int connectionPoolSize;

    @Value("${identity-service.always-refresh-token:false}")
    boolean alwaysRefreshToken;

    @Value("${identity-service.register-node-at-startup:false}")
    boolean registerNodeAtStartup;

    @Value("${identity-service.register-node-period:-1}")
    int registerNodePeriod;

    @Value("${identity-service.token-store:#{null}}")
    String tokenStore;

    @Value("${identity-service.principal-attribute:#{null}}")
    String principalAttribute;

    @Value("${identity-service.turn-off-change-session-id-on-login:false}")
    boolean turnOffChangeSessionIdOnLogin;

    @Value("${identity-service.token-minimum-time-to-live:0}")
    int tokenMinimumTimeToLive;

    @Value("${identity-service.min-time-between-jwks-requests:10}")
    int minTimeBetweenJwksRequests;

    @Value("${identity-service.public-key-cache-ttl:86400}")
    int publicKeyCacheTtl;

    @Value("${identity-service.enable-pkce:false}")
    boolean enablePkce;

    @Value("${identity-service.ignore-oauth-query-parameter:false}")
    boolean ignoreOAuthQueryParameter;

    @Value("${identity-service.credentials.secret:}")
    String credentialsSecret;

    @Value("${identity-service.credentials.provider:#{null}}")
    String credentialsProvider;

    @Value("${identity-service.adminUsername:admin}")
    String adminUsername;

    @Value("${identity-service.adminPassword:admin}")
    String adminPassword;

    public String getAdminUsername()
    {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername)
    {
        this.adminUsername = adminUsername;
    }

    public String getAdminPassword()
    {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword)
    {
        this.adminPassword = adminPassword;
    }

    public AdapterConfig getAdapterConfig()
    {
        if (adapterConfig != null)
        {
            return adapterConfig;
        }

        AdapterConfig adapterConfig = new AdapterConfig();
        adapterConfig.setRealm(realm);
        adapterConfig.setRealmKey(realmKey);
        adapterConfig.setAuthServerUrl(authServerUrl);
        adapterConfig.setSslRequired(sslRequired);
        adapterConfig.setConfidentialPort(confidentialPort);
        adapterConfig.setResource(resource);
        adapterConfig.setUseResourceRoleMappings(useResourceRoleMappings);
        adapterConfig.setCors(enableCors);
        adapterConfig.setCorsMaxAge(corsMaxAge);
        adapterConfig.setCorsAllowedHeaders(corsAllowedHeaders);
        adapterConfig.setCorsAllowedMethods(corsAllowedMethods);
        adapterConfig.setCorsExposedHeaders(corsExposedHeaders);
        adapterConfig.setExposeToken(exposeToken);
        adapterConfig.setBearerOnly(bearerOnly);
        adapterConfig.setAutodetectBearerOnly(autodetectBearerOnly);
        adapterConfig.setEnableBasicAuth(enableBasicAuth);
        adapterConfig.setPublicClient(publicClient);
        adapterConfig.setAllowAnyHostname(allowAnyHostname);
        adapterConfig.setDisableTrustManager(disableTrustManager);
        adapterConfig.setTruststore(truststore);
        adapterConfig.setTruststorePassword(truststorePassword);
        adapterConfig.setClientKeystore(clientKeystore);
        adapterConfig.setClientKeystorePassword(clientKeystorePassword);
        adapterConfig.setClientKeyPassword(clientKeyPassword);
        adapterConfig.setConnectionPoolSize(connectionPoolSize);
        adapterConfig.setAlwaysRefreshToken(alwaysRefreshToken);
        adapterConfig.setRegisterNodeAtStartup(registerNodeAtStartup);
        adapterConfig.setRegisterNodePeriod(registerNodePeriod);
        adapterConfig.setTokenStore(tokenStore);
        adapterConfig.setPrincipalAttribute(principalAttribute);
        adapterConfig.setTurnOffChangeSessionIdOnLogin(turnOffChangeSessionIdOnLogin);
        adapterConfig.setTokenMinimumTimeToLive(tokenMinimumTimeToLive);
        adapterConfig.setMinTimeBetweenJwksRequests(minTimeBetweenJwksRequests);
        adapterConfig.setPublicKeyCacheTtl(publicKeyCacheTtl);
        adapterConfig.setPkce(enablePkce);
        adapterConfig.setIgnoreOAuthQueryParameter(ignoreOAuthQueryParameter);

        // programmatically build the more complex objects i.e. credentials
        Map<String, Object> credentials = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        if (credentialsSecret != null)
        {
            credentials.put("secret", credentialsSecret);
        }
        else
        {
            credentials.put("secret", "");
        }

        if (credentialsProvider != null && !credentialsProvider.isEmpty())
        {
            credentials.put("provider", credentialsProvider);
        }

        adapterConfig.setCredentials(credentials);

        this.adapterConfig = adapterConfig;

        return adapterConfig;
    }
}
