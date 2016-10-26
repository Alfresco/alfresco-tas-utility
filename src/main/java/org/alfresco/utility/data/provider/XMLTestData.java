package org.alfresco.utility.data.provider;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.alfresco.utility.model.QueryModel;

@XmlRootElement(name = "testData")
public class XMLTestData
{
    private List<QueryModel> queries;
    private List<XMLSiteData> sites = new ArrayList<XMLSiteData>();

    @XmlElementWrapper
    @XmlElement(name = "query")
    public List<QueryModel> getQueries()
    {
        return queries;
    }

    public void setQueries(List<QueryModel> queries)
    {
        this.queries = queries;
    }

    @XmlElementWrapper
    @XmlElement(name = "site")
    public List<XMLSiteData> getSites()
    {
        return sites;
    }

    public void setSites(List<XMLSiteData> sites)
    {
        this.sites = sites;
    }
}
