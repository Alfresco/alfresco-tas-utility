package org.alfresco.utility.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Bogdan Bocancea
 */
public class CustomAspectModel extends TestModel
{
    @JsonProperty(required = true)
    private String name;
    
    @JsonProperty
    private String prefixedName;
    
    @JsonProperty
    private String title;
    
    @JsonProperty(required = true)
    private List<CustomAspectPropertiesModel> properties;
    
    public CustomAspectModel()
    {
        
    }
    
    public CustomAspectModel(String aspectName)
    {
        setName(aspectName);
    }
    
    public CustomAspectModel(String aspectName, String title)
    {
        setName(aspectName);
        setTitle(title);
    }
    
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPrefixedName()
    {
        return prefixedName;
    }

    public void setPrefixedName(String prefixedName)
    {
        this.prefixedName = prefixedName;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }
    
    public List<CustomAspectPropertiesModel> getProperties()
    {
        return properties;
    }

    public void setproperties(List<CustomAspectPropertiesModel> properties)
    {
        this.properties = properties;
    }
    
    public CustomAspectPropertiesModel getPropertyByName(String propertyName)
    {
        CustomAspectPropertiesModel foundProperty = null;
        for (CustomAspectPropertiesModel propertyModel : getProperties())
        {
            if (propertyModel.getName().equals(propertyName))
            {
                return propertyModel;
            }
        }
        return foundProperty;
    }
}
