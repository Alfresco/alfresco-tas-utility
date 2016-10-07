package org.alfresco.utility.model;

import java.util.List;

import org.alfresco.utility.constants.NetworkSubscriptionLevel;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Cristina Axinte on 9/26/2016.
 */
public class NetworkModel
{
    @JsonProperty(required = true)
    private String id;

    private boolean homeNetwork;

    @JsonProperty(required = true)
    private boolean isEnabled;

    private String createdAt;
    private boolean paidNetwork;
    private NetworkSubscriptionLevel subscriptionLevel;

    private List<QuotaModel> quotas;

    public NetworkModel()
    {
    }

    public NetworkModel(String id, boolean isEnabled)
    {
        this.id = id;
        this.isEnabled = isEnabled;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public boolean isHomeNetwork()
    {
        return homeNetwork;
    }

    public void setHomeNetwork(boolean homeNetwork)
    {
        this.homeNetwork = homeNetwork;
    }

    public boolean isEnabled()
    {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled)
    {
        this.isEnabled = isEnabled;
    }

    public String getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(String createdAt)
    {
        this.createdAt = createdAt;
    }

    public boolean isPaidNetwork()
    {
        return paidNetwork;
    }

    public void setPaidNetwork(boolean paidNetwork)
    {
        this.paidNetwork = paidNetwork;
    }

    public NetworkSubscriptionLevel getSubscriptionLevel()
    {
        return subscriptionLevel;
    }

    public void setSubscriptionLevel(NetworkSubscriptionLevel subscriptionLevel)
    {
        this.subscriptionLevel = subscriptionLevel;
    }

    public List<QuotaModel> getQuotas()
    {
        return quotas;
    }

    public void setQuotas(List<QuotaModel> quotas)
    {
        this.quotas = quotas;
    }

}
