package org.alfresco.utility.data.provider;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "query")
public class XMLQuery
{    
    private String search;
    private int results;

    @XmlAttribute(name = "search")
    public String getSearch()
    {
        return search;
    }

    public void setSearch(String search)
    {
        this.search = search;
    }

    @XmlAttribute(name = "expectedResults")
    public int getResults()
    {
        return results;
    }

    public void setResults(int results)
    {
        this.results = results;
    }
}
