package org.alfresco.utility.data;

/**
 * POJO for Alfresco Identity Service Token
 */
public class AisToken
{
    private String token;
    private String refreshToken;
    private long creationTime;
    private long expiresIn;

    public AisToken(String token, String refreshToken, long creationTime, long expiresIn)
    {
        this.token = token;
        this.creationTime = creationTime;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public String getRefreshToken()
    {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken)
    {
        this.refreshToken = refreshToken;
    }

    public long getCreationTime()
    {
        return creationTime;
    }

    public void setCreationTime(long creationTime)
    {
        this.creationTime = creationTime;
    }

    public long getExpiresIn()
    {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn)
    {
        this.expiresIn = expiresIn;
    }

    public long getExpirationTime()
    {
        return creationTime + expiresIn;
    }
}
