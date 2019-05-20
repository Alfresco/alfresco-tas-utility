package org.alfresco.utility.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Bogdan Bocancea
 */
public class CustomAspectPropertiesModel extends TestModel
{
    @JsonProperty(required = true)
    private String name;
    private String prefixedName;
    private String title = "";
    private String description = "";
    private String dataType;
    private String facetable;
    private String indexTokenisationMode;
    private boolean mandatory = false;
    private boolean multiValued = false;
    private boolean indexed = true;
    private boolean mandatoryEnforced = false;

    public CustomAspectPropertiesModel()
    {
        
    }
    
    public CustomAspectPropertiesModel(String name)
    {
        setName(name);
        setDataType(dataType);
    }
    
    public CustomAspectPropertiesModel(String name, String dataType)
    {
        setName(name);
        setDataType(dataType);
    }

    public CustomAspectPropertiesModel(String name, String title, String dataType)
    {
        setName(name);
        setTitle(title);
        setDataType(dataType);
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

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDataType()
    {
        return dataType;
    }

    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }

    public String isFacetable()
    {
        return facetable;
    }

    public void setFacetable(String facetable)
    {
        this.facetable = facetable;
    }

    public String isIndexTokenisationMode()
    {
        return indexTokenisationMode;
    }

    public void setIndexTokenisationMode(String indexTokenisationMode)
    {
        this.indexTokenisationMode = indexTokenisationMode;
    }

    public boolean isMandatory()
    {
        return mandatory;
    }

    public void setMandatory(boolean mandatory)
    {
        this.mandatory = mandatory;
    }

    public boolean isMultiValued()
    {
        return multiValued;
    }

    public void setMultiValued(boolean multiValued)
    {
        this.multiValued = multiValued;
    }

    public boolean isIndexed()
    {
        return indexed;
    }

    public void setIndexed(boolean indexed)
    {
        this.indexed = indexed;
    }

    public boolean isMandatoryEnforced()
    {
        return mandatoryEnforced;
    }

    public void setMandatoryEnforced(boolean mandatoryEnforced)
    {
        this.mandatoryEnforced = mandatoryEnforced;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
