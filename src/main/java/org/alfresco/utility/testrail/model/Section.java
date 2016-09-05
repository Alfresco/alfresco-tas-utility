package org.alfresco.utility.testrail.model;

/**
 * Represents a Section from TestRail
 * {
 * "id": 1,
 * "suite_id": 1,
 * "name": "rest-api",
 * "description": null,
 * "parent_id": null,
 * "display_order": 1,
 * "depth": 0
 * }
 */
public class Section
{
    private int id;
    private int suite_id;
    private String name;
    private String description;
    private int parent_id;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getSuite_id()
    {
        return suite_id;
    }

    public void setSuite_id(int suite_id)
    {
        this.suite_id = suite_id;
    }

    public String getName()
    {
        return name;
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

    public int getParent_id()
    {
        return parent_id;
    }

    public void setParent_id(int parent_id)
    {
        this.parent_id = parent_id;
    }

    public int getDisplay_order()
    {
        return display_order;
    }

    public void setDisplay_order(int display_order)
    {
        this.display_order = display_order;
    }

    public int getDepth()
    {
        return depth;
    }

    public void setDepth(int depth)
    {
        this.depth = depth;
    }

    private int display_order;
    private int depth;
}
