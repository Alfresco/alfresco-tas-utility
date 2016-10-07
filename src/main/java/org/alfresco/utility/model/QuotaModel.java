package org.alfresco.utility.model;

/**
 * @author Cristina Axinte on 9/26/2016.
 */
public class QuotaModel extends TestModel
{
    private String id;
    private int limit;
    private int usage;
    
    public QuotaModel()
    {  
    }

    public String getId()
    {
        return id;
    }

    public int getLimit()
    {
        return limit;
    }

    public void setLimit(int limit)
    {
        this.limit = limit;
    }

    public int getUsage()
    {
        return usage;
    }

    public void setUsage(int usage)
    {
        this.usage = usage;
    }
}
