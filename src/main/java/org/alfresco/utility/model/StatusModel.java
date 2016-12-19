package org.alfresco.utility.model;

import org.testng.Assert;

public class StatusModel extends TestModel
{
    private int code = 0;
    private String name = "n/a";
    private String description = "n/a";    

    public int getCode()
    {
        return code;
    }

    public String getName()
    {
        return name;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public StatusModel hasName(String name)
    {
        if(!getName().contains(name))
        Assert.fail(String.format("Expected [%s] error to be found in actual status name returned by server: %s", name, getName()));
        
        return this;
    }
    
}