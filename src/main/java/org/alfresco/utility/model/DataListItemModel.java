package org.alfresco.utility.model;

import java.util.Map;

/**
 * {@link https://community.alfresco.com/thread/202546-cmis-data-list}
 */
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
