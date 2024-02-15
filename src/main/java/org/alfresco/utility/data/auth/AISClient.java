package org.alfresco.utility.data.auth;

import static java.lang.String.format;
import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

import static org.springframework.web.util.UriComponentsBuilder.fromUri;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.Gson;

import org.alfresco.utility.TasAisProperties;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;

class AISClient
{
    private final String clientId;
    private final String adminUsername;
    private final String adminPassword;
    private final URI tokenUri;
    private final URI usersUri;
    private final HttpClient httpClient;
    private final boolean isKeycloak;
    private final TasAisProperties aisProperties;
    private static final Gson GSON = new Gson();

    AISClient(String clientId, String adminUsername, String adminPassword, URI tokenUri, URI usersUri, HttpClient httpClient, boolean isKeycloak, TasAisProperties aisProperties)
    {
        this.clientId = requireNonNull(clientId);
        this.adminUsername = requireNonNull(adminUsername);
        this.adminPassword = requireNonNull(adminPassword);
        this.tokenUri = requireNonNull(tokenUri);
        this.usersUri = usersUri;
        this.httpClient = requireNonNull(httpClient);
        this.isKeycloak = requireNonNull(isKeycloak);
        this.aisProperties = requireNonNull(aisProperties);
    }

    Map<String, ?> authorizeUser(String username, String password)
    {
        Map<String, String> requestForm = Map.of(
            "grant_type", "password",
            "username", username,
            "password", password,
            "scope", "openid",
            "client_id", clientId);
        if(!isKeycloak && StringUtils.isNotBlank(aisProperties.getAudience()))
        {
            requestForm = new HashMap<>(requestForm);
            requestForm.put("audience", aisProperties.getAudience());
        }
        final HttpRequest request = createFormPostRequest(tokenUri, requestForm);

        String response = getResponse(request, requireOkStatus(r -> format("Failed to obtain AIS User (%s) Access Token: %s", username, r.body())));

        return GSON.fromJson(response, Map.class);
    }

    List<Map<String, Object>> findUser(String username)
    {
        final URI uri = fromUri(usersUri)
                .queryParam("username", username)
                .queryParam("first", 0)
                .queryParam("max", Integer.MAX_VALUE)
                .build().toUri();
        final HttpRequest request = authenticatedRequestBuilder(uri).GET().build();

        String response = getResponse(request, requireOkStatus(r -> "Failed to search for users: " + r.body()));

        return GSON.fromJson(response, List.class);
    }

    void createUser(String username, String password, String firstName, String lastName)
    {
        final HttpRequest request = authenticatedRequestBuilder(usersUri)
                .header("Content-Type", "application/json;charset=UTF-8")
                .POST(asJson(Map.of(
                        "username", username,
                        "firstName", firstName,
                        "lastName", lastName,
                        "enabled", true,
                        "credentials", List.of(Map.of(
                                "type", "password",
                                "value", password,
                                "temporary", false)))))
                .build();

        getResponse(request, requireCreatedStatus(r -> "Failed to create AIS user: " + r.body()));
    }

    void deleteUser(String userId)
    {
        final URI uri = fromUri(usersUri).pathSegment(userId).build().toUri();
        final HttpRequest request = authenticatedRequestBuilder(uri).DELETE().build();

        getResponse(request, requireNoContentStatus(r -> "Failed to delete AIS user: " + r.body()));
    }

    void updateUser(Map<String, Object> user)
    {
        final URI uri = fromUri(usersUri).pathSegment(extractUserId(user)).build().toUri();
        final HttpRequest request = authenticatedRequestBuilder(uri)
                .header("Content-Type", "application/json;charset=UTF-8")
                .PUT(asJson(user)).build();

        getResponse(request, requireNoContentStatus(r -> "Failed to save AIS user: " + r.body()));
    }

    private HttpRequest.Builder authenticatedRequestBuilder(URI uri)
    {
        final String accessToken = getAdminAccessToken();
        return HttpRequest.newBuilder(uri)
                          .header("Authorization", "Bearer " + accessToken)
                          .header("Accept", "application/json");
    }

    private String getAdminAccessToken()
    {
        final Map<String, ?> authorization = authorizeUser(adminUsername, adminPassword);
        return (String) authorization.get("access_token");
    }

    private Consumer<HttpResponse<?>> requireNoContentStatus(Function<HttpResponse<?>, String> message)
    {
        return requireStatus(204, message);
    }

    private Consumer<HttpResponse<?>> requireCreatedStatus(Function<HttpResponse<?>, String> message)
    {
        return requireStatus(201, message);
    }

    private Consumer<HttpResponse<?>> requireOkStatus(Function<HttpResponse<?>, String> message)
    {
        return requireStatus(200, message);
    }

    private Consumer<HttpResponse<?>> requireStatus(int expectedStatus, Function<HttpResponse<?>, String> message)
    {
        return r -> Assert.assertEquals(message.apply(r), expectedStatus, r.statusCode());
    }

    private String getResponse(HttpRequest request, Consumer<HttpResponse<?>>... responseValidators)
    {
        try
        {
            final HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
            Stream.of(responseValidators).forEach(v -> v.accept(response));
            return response.body();
        } catch (IOException e)
        {
            throw new IllegalStateException(e);
        } catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        }
    }

    private HttpRequest createFormPostRequest(URI uri, Map<String, String> formParams)
    {
        String encodedForm = formParams.entrySet()
                                       .stream()
                                       .map(e -> e.getKey() + "=" + encode(e.getValue(), UTF_8))
                                       .collect(Collectors.joining("&"));

        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder()
                          .uri(uri)
                          .headers("Content-Type", "application/x-www-form-urlencoded")
                          .header("Accept", "application/json")
                          .POST(BodyPublishers.ofString(encodedForm));

        if(StringUtils.isNotBlank(aisProperties.getResource()) && StringUtils.isNotBlank(aisProperties.getCredentialsSecret()))
        {
            httpRequestBuilder
                .header("Authorization", "Basic " + new String(Base64.encodeBase64((aisProperties.getResource() + ":" + aisProperties.getCredentialsSecret()).getBytes())));
        }

        return httpRequestBuilder.build();
    }

    static void setEnabled(Map<String, Object> user, boolean isEnabled)
    {
        user.put("enabled", isEnabled);
    }

    static String extractUserId(Map<String, ?> user)
    {
        return Optional.ofNullable(user)
                       .map(u -> u.get("id"))
                       .filter(String.class::isInstance)
                       .map(String.class::cast)
                       .orElse(null);
    }

    private static BodyPublisher asJson(Map<String, ?> map)
    {
        return BodyPublishers.ofString(GSON.toJson(map));
    }
}
