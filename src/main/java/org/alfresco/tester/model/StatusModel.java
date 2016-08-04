package org.alfresco.tester.model;

public class StatusModel
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

}
