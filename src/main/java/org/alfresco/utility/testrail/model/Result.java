package org.alfresco.utility.testrail.model;

/**
 * "id": 49922,
 * "test_id": 531635,
 * "status_id": 1,
 * "created_by": 65,
 * "created_on": 1484598969,
 * "assignedto_id": null,
 * "comment": "Executed by awesome TAS project",
 * "version": null,
 * "elapsed": null,
 * "defects": null
 */
public class Result
{
    private long id;
    private long test_id;
    private int status_id;
    private String created_by;
    private long created_on;
    private String assignedto_id;
    private String comment;
    private String version;
    private String elapsed;
    private String defects;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getTest_id()
    {
        return test_id;
    }

    public void setTest_id(long test_id)
    {
        this.test_id = test_id;
    }

    public int getStatus_id()
    {
        return status_id;
    }

    public void setStatus_id(int status_id)
    {
        this.status_id = status_id;
    }

    public String getCreated_by()
    {
        return created_by;
    }

    public void setCreated_by(String created_by)
    {
        this.created_by = created_by;
    }

    public long getCreated_on()
    {
        return created_on;
    }

    public void setCreated_on(long created_on)
    {
        this.created_on = created_on;
    }

    public String getAssignedto_id()
    {
        return assignedto_id;
    }

    public void setAssignedto_id(String assignedto_id)
    {
        this.assignedto_id = assignedto_id;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getElapsed()
    {
        return elapsed;
    }

    public void setElapsed(String elapsed)
    {
        this.elapsed = elapsed;
    }

    public String getDefects()
    {
        return defects;
    }

    public void setDefects(String defects)
    {
        this.defects = defects;
    }   
}
