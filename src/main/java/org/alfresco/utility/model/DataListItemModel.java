package org.alfresco.utility.model;

import java.util.Map;

public class DataListItemModel extends ContentModel
{
    private Map<String, Object> itemProperties;
    
    public DataListItemModel(Map<String, Object> itemProperties)
    {
        setItemProperties(itemProperties);
    }
    
    public Map<String, Object> getItemProperties()
    {
        return itemProperties;
    }
    public void setItemProperties(Map<String, Object> itemProperties)
    {
        this.itemProperties = itemProperties;
    }
}
