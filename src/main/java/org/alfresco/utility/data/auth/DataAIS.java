package org.alfresco.utility.data.auth;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.core.Response;

import org.alfresco.utility.TasAisProperties;
import org.alfresco.utility.data.AisToken;
import org.alfresco.utility.model.UserModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.HttpClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.junit.Assert;
import org.keycloak.adapters.HttpClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * DataAIS provides access to Alfresco Identity Service users resource.
 * Check that {@link #isEnabled()} is true before calling to {@link #perform()}.
 * The AIS support is enabled by setting <code>identity-service.auth-server-url</code> property
 * and can be further configured by setting properties defined in {@link TasAisProperties}.
 */
@Service
@Scope(value = "prototype")
public class DataAIS implements InitializingBean
{
    private static Log LOG = LogFactory.getLog(DataAIS.class);
    private UsersResource usersResource;
    private AuthzClient authzClient;
    private boolean enabled;
    private static HashMap<Integer, AisToken> aisTokens = new HashMap<>();
    private static final int TIMEOUT_DELTA_MILLISECONDS = 5000;

    @Autowired
    private TasAisProperties aisProperties;

    @Override
    public void afterPropertiesSet()
    {
        AdapterConfig aisAdapterConfig = aisProperties.getAdapterConfig();
        String authServerUrl = aisAdapterConfig.getAuthServerUrl();
        if (authServerUrl != null && !authServerUrl.isEmpty())
        {
            enabled = true;
        }

        // Check the minimal set of properties required to communicate with the keycloak server
        if (enabled)
        {
            String realm = aisAdapterConfig.getRealm();
            String resource = aisAdapterConfig.getResource();
            String adminUsername = aisProperties.getAdminUsername();
            String adminPassword = aisProperties.getAdminPassword();

            Assert.assertTrue("AIS realm can not be empty", realm!=null && !realm.isEmpty());
            Assert.assertTrue("AIS resource can not be empty", resource!=null && !resource.isEmpty());
            Assert.assertTrue("AIS adminUsername can not be empty", adminUsername!=null && !adminUsername.isEmpty());
            Assert.assertTrue("AIS adminPassword can not be empty", adminPassword!=null && !adminPassword.isEmpty());

            LOG.info(String.format("[AlfrescoIdentityService] Building Keycloak clients. Url= %s ", authServerUrl));

            // Configure http client
            HttpClient httpClient = new HttpClientBuilder()
                    .build(aisAdapterConfig);
            ApacheHttpClient4Engine httpEngine = new ApacheHttpClient4Engine(httpClient);

            // Configure usersResource
            Keycloak  keycloak = KeycloakBuilder
                .builder()
                .serverUrl(authServerUrl)
                .realm(realm)
                .username(adminUsername)
                .password(adminPassword)
                .clientId(resource)
                .resteasyClient(new ResteasyClientBuilder()
                    .httpEngine(httpEngine)
                    .build())
                .build();
            usersResource = keycloak.realm(realm).users();

            // Configure authzClient
            Configuration authzConfig = new Configuration(authServerUrl, realm, resource,
                aisAdapterConfig.getCredentials(), httpClient);
            authzClient = AuthzClient.create(authzConfig);
        }
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setUsersResource(UsersResource usersResource)
    {
        this.usersResource = usersResource;
    }

    public void setAuthzClient(AuthzClient authzClient)
    {
        this.authzClient = authzClient;
    }

    public void setAisProperties(TasAisProperties aisProperties)
    {
        this.aisProperties = aisProperties;
    }

    public DataAIS.Builder perform()
    {
        Assert.assertTrue("[AlfrescoIdentityService] AlfrescoIdentityService support is disabled.", isEnabled());
        return new DataAIS.Builder();
    }

    public class Builder implements UserManageable
    {
        private Builder()
        {
        }

        @Override
        public Builder createUser(UserModel user)
        {
            LOG.info(String.format("[AlfrescoIdentityService] Add user %s", user.getUsername()));

            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(user.getPassword());
            credential.setTemporary(false);

            UserRepresentation userRepresentation = new UserRepresentation();
            userRepresentation.setUsername(user.getUsername());
            userRepresentation.setFirstName(user.getFirstName());
            userRepresentation.setLastName(user.getLastName());
            userRepresentation.setCredentials(Arrays.asList(credential));
            userRepresentation.setEnabled(true);

            Response response = usersResource.create(userRepresentation);
            Assert.assertTrue("Failed to create user in Keycloak: " + response.getStatusInfo(),
                    response.getStatusInfo().equals(Response.Status.CREATED));
            response.close();

            return this;
        }

        @Override
        public Builder deleteUser(UserModel user)
        {
            LOG.info(String.format("[AlfrescoIdentityService] Delete user %s", user.getUsername()));

            UserRepresentation userRepresentation = findUserByUsername(user.getUsername());
            if (userRepresentation != null)
            {
                removeTokenForUser(generateTokenKey(user));

                Response response = usersResource.delete(userRepresentation.getId());
                response.close();
            }
            return this;
        }

        @Override
        public Builder updateUser(UserModel user, HashMap<String, String> attributes)
        {
            throw new UnsupportedOperationException("Not implemented.");
        }

        @Override
        public Builder assertUserExists(UserModel user)
        {
            LOG.info(String.format("[AlfrescoIdentityService] Assert user %s exists", user.getUsername()));
            Assert.assertNotNull(findUserByUsername(user.getUsername()));
            return this;
        }

        @Override
        public Builder assertUserDoesNotExist(UserModel user)
        {
            LOG.info(String.format("[AlfrescoIdentityService] Assert user %s does not exists", user.getUsername()));
            Assert.assertNull(findUserByUsername(user.getUsername()));
            return this;
        }

        public Builder disableUser(UserModel user)
        {
            LOG.info(String.format("[AlfrescoIdentityService] Disable user %s", user.getUsername()));
            UserRepresentation userRepresentation = findUserByUsername(user.getUsername());
            userRepresentation.setEnabled(false);
            usersResource.get(userRepresentation.getId()).update(userRepresentation);
            removeTokenForUser(generateTokenKey(user));
            return this;
        }

        public Builder enableUser(UserModel user)
        {
            LOG.info(String.format("[AlfrescoIdentityService] Disable user %s", user.getUsername()));
            UserRepresentation userRepresentation = findUserByUsername(user.getUsername());
            userRepresentation.setEnabled(true);
            usersResource.get(userRepresentation.getId()).update(userRepresentation);
            return this;
        }

        public AccessTokenResponse obtainAccessToken(UserModel user)
        {
            LOG.info(String.format("[AlfrescoIdentityService] Obtain access token for user %s", user.getUsername()));
            return authzClient.obtainAccessToken(user.getUsername(), user.getPassword());
        }

        public UserRepresentation findUserByUsername(String username)
        {
            UserRepresentation user = null;
            List<UserRepresentation> ur = usersResource.search(username, null, null, null, 0, Integer.MAX_VALUE);
            if (ur.size() == 1)
            {
                user = ur.get(0);
            }

            if (ur.size() > 1) // try to be more specific
            {
                for (UserRepresentation rep : ur)
                {
                    if (rep.getUsername().equalsIgnoreCase(username))
                    {
                        return rep;
                    }
                }
            }

            return user;
        }

        public synchronized void addTokenForUser(Integer key, AccessTokenResponse tokenResponse)
        {
            Long currentTime = System.currentTimeMillis();
            AisToken token = new AisToken(tokenResponse.getToken(), tokenResponse.getRefreshToken(), currentTime, tokenResponse.getExpiresIn() * 1000);

            aisTokens.put(key, token);
        }

        public synchronized void removeTokenForUser(Integer key)
        {
            aisTokens.remove(key);
        }

        public Boolean checkTokenValidity(Integer key)
        {
            Long currentTime = System.currentTimeMillis();

            if (aisTokens.containsKey(key))
            {
                AisToken token = aisTokens.get(key);
                if (currentTime < (token.getExpirationTime() - TIMEOUT_DELTA_MILLISECONDS))
                {
                    return true;
                }
            }

            return false;
        }

        /**
         * Get user access token from cache or request a new token if the
         * existing one is not valid
         * 
         * @param user
         * @return
         */
        public AisToken getAccessToken(UserModel user)
        {
            Integer key = generateTokenKey(user);

            if(!checkTokenValidity(key))
            {
                //Get and add new user token
                AccessTokenResponse tokenReponse = obtainAccessToken(user);
                addTokenForUser(key, tokenReponse);
            }

            return aisTokens.get(key);
        }

        /**
         * Generate keys for the token caching mechanism
         * @param user
         * @return
         */
        private Integer generateTokenKey(UserModel user)
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((user.getUsername() == null) ? 0 : user.getUsername().hashCode());
            result = prime * result + ((user.getPassword() == null) ? 0 : user.getPassword().hashCode());
            return result;
        }
    }
}
