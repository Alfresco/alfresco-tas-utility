package org.alfresco.utility.data;

import java.util.HashMap;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.CmisObject;

/**
 * Use this class if you want to update the properties of a CMIS Document of CMIS Folder
 * 
 * You can use this class in correlation with {@link DataContent#createCustomContent(org.alfresco.utility.model.ContentModel, String, CustomObjectTypeProperties)}
 *
 */
public class CustomObjectTypeProperties
{
    private Map<String, Object> properties;

    public CustomObjectTypeProperties()
    {
        properties = new HashMap<String, Object>();
    }

    public CustomObjectTypeProperties addProperty(String key, Object value)
    {
        properties.put(key, value);
        return this;
    }

    public void applyPropertiesTo(CmisObject cmisObject)
    {
        if (!properties.isEmpty())
        {
            cmisObject.updateProperties(properties);
        }
    }

}
