package org.alfresco.utility.data.auth;

import static org.alfresco.utility.data.auth.AISClient.extractUserId;
import static org.alfresco.utility.data.auth.AISClient.setEnabled;
import static org.springframework.web.util.UriComponentsBuilder.fromUri;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

import java.net.URI;
import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.utility.TasAisProperties;
import org.alfresco.utility.data.AisToken;
import org.alfresco.utility.model.UserModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
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
    private static final Log LOG = LogFactory.getLog(DataAIS.class);
    private static final HashMap<Integer, AisToken> aisTokens = new HashMap<>();
    private static final int TIMEOUT_DELTA_MILLISECONDS = 5000;
    private boolean enabled;

    @Autowired
    private TasAisProperties aisProperties;
    private AISClient aisClient;

    @Override
    public void afterPropertiesSet()
    {
        String authServerUrl = aisProperties.getAuthServerUrl();
        if (authServerUrl != null && !authServerUrl.isEmpty())
        {
            enabled = true;
        }

        // Check the minimal set of properties required to communicate with the AIS server
        if (enabled)
        {
            String realm = aisProperties.getRealm();
            String resource = aisProperties.getResource();
            String adminUsername = aisProperties.getAdminUsername();
            String adminPassword = aisProperties.getAdminPassword();

            Assert.assertTrue("AIS realm can not be empty", realm!=null && !realm.isEmpty());
            Assert.assertTrue("AIS resource can not be empty", resource!=null && !resource.isEmpty());
            Assert.assertTrue("AIS adminUsername can not be empty", adminUsername!=null && !adminUsername.isEmpty());
            Assert.assertTrue("AIS adminPassword can not be empty", adminPassword!=null && !adminPassword.isEmpty());

            LOG.info(String.format("[AlfrescoIdentityService] Building AIS clients. Url= %s ", authServerUrl));

            URI issuerUri = fromUriString(authServerUrl).pathSegment("realms", realm).build().toUri();
            URI tokenUri = fromUri(issuerUri).pathSegment("protocol", "openid-connect", "token").build().toUri();
            URI usersUri = fromUriString(authServerUrl).pathSegment("admin", "realms", realm, "users").build().toUri();

            // Configure http client
            final HttpClient httpClient = HttpClient.newHttpClient();
            aisClient = new AISClient(resource, adminUsername, adminPassword, tokenUri, usersUri, httpClient);
        }
    }

    public boolean isEnabled()
    {
        return enabled;
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

            aisClient.createUser(user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName());
            return this;
        }

        @Override
        public Builder deleteUser(UserModel user)
        {
            LOG.info(String.format("[AlfrescoIdentityService] Delete user %s", user.getUsername()));

            String userId = extractUserId(findUserByUsername(user.getUsername()));
            if (userId != null)
            {
                removeTokenForUser(generateTokenKey(user));

                aisClient.deleteUser(userId);
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
            Map<String, Object> userRepresentation = findUserByUsername(user.getUsername());
            setEnabled(userRepresentation, false);
            aisClient.updateUser(userRepresentation);
            removeTokenForUser(generateTokenKey(user));
            return this;
        }

        public Builder enableUser(UserModel user)
        {
            LOG.info(String.format("[AlfrescoIdentityService] Enable user %s", user.getUsername()));
            Map<String, Object> userRepresentation = findUserByUsername(user.getUsername());
            setEnabled(userRepresentation, true);
            aisClient.updateUser(userRepresentation);
            return this;
        }

        private Map<String, ?> obtainAccessToken(UserModel user)
        {
            LOG.info(String.format("[AlfrescoIdentityService] Obtain access token for user %s", user.getUsername()));
            return aisClient.authorizeUser(user.getUsername(), user.getPassword());
        }

        private Map<String, Object> findUserByUsername(String username)
        {
            final List<Map<String, Object>> matchingUsers = aisClient.findUser(username);

            if (matchingUsers.size() == 1)
            {
                return matchingUsers.get(0);
            }

            if (matchingUsers.size() > 1) // try to be more specific
            {
                for (Map<String, Object> user : matchingUsers)
                {
                    if (username.equalsIgnoreCase((String)user.get("username")))
                    {
                        return user;
                    }
                }
            }

            return null;
        }

        public synchronized void addTokenForUser(Integer key, Map<String, ?> tokenResponse)
        {
            long currentTime = System.currentTimeMillis();
            //AisToken token = new AisToken(tokenResponse.get("access_token"), tokenResponse.get("refresh_token"), currentTime, tokenResponse.getExpiresIn() * 1000);
            AisToken token = new AisToken(
                    (String) tokenResponse.get("access_token"),
                    (String) tokenResponse.get("refresh_token"),
                    currentTime,
                    ((Number)tokenResponse.get("expires_in")).longValue() * 1000);

            aisTokens.put(key, token);
        }

        public synchronized void removeTokenForUser(Integer key)
        {
            aisTokens.remove(key);
        }

        public Boolean checkTokenValidity(Integer key)
        {
            long currentTime = System.currentTimeMillis();

            if (aisTokens.containsKey(key))
            {
                AisToken token = aisTokens.get(key);
                return currentTime < (token.getExpirationTime() - TIMEOUT_DELTA_MILLISECONDS);
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
                Map<String, ?> tokenResponse = obtainAccessToken(user);
                addTokenForUser(key, tokenResponse);
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
