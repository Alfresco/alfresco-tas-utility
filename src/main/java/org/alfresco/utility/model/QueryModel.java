package org.alfresco.utility.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Represents a simple query object that will contain the search text string and the expected results count returned by that query
 */
@XmlType(name = "query")
public class QueryModel
{
    private String value;
    private long results;

    @XmlAttribute(name = "value")
    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    @XmlAttribute(name = "expectedResults")
    public long getResults()
    {
        return results;
    }

    public void setResults(long results)
    {
        this.results = results;
    }
}
