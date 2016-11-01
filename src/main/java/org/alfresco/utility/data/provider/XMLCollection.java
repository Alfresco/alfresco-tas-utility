package org.alfresco.utility.data.provider;

import java.util.ArrayList;
import java.util.List;

public abstract class XMLCollection
{
    protected List<XMLDataItem> entireStructure = new ArrayList<>();

    public List<XMLDataItem> getEntireStructure()
    {
        if (this.entireStructure.isEmpty())
        {
            this.entireStructure = getImbricatedData();
        }
        return entireStructure;
    }

    protected abstract List<XMLDataItem> getImbricatedData();

    protected void bulkAddToCollection(List<XMLDataItem> collection, List<XMLDataItem> entireStructure)
    {
        entireStructure.addAll(collection);        
    }
}
