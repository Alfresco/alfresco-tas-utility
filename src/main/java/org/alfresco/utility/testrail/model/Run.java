package org.alfresco.utility.testrail.model;

/**
 * Represents on Run from TestRail
 * {
 * "id": 1,
 * "suite_id": 1,
 * "name": "Test Run 9/3/2016",
 * "description": null,
 * "milestone_id": null,
 * "assignedto_id": null,
 * "include_all": false,
 * "is_completed": true,
 * "completed_on": 1472920906,
 * "config": null,
 * "config_ids": [],
 * "passed_count": 0,
 * "blocked_count": 0,
 * "untested_count": 4,
 * "retest_count": 0,
 * "failed_count": 1,
 * "custom_status1_count": 0,
 * "custom_status2_count": 0,
 * "custom_status3_count": 0,
 * "custom_status4_count": 0,
 * "custom_status5_count": 0,
 * "custom_status6_count": 0,
 * "custom_status7_count": 0,
 * "project_id": 1,
 * "plan_id": null,
 * "created_on": 1472920404,
 * "created_by": 1,
 * "url": "https://pauly.testrail.net/index.php?/runs/view/1"
 * }
 */
public class Run
{

    private int id;
    private int suite_id;
    private String name;
    private String description;
    private int milestone_id;
    private int assignedto_id;
    private boolean include_all;
    private boolean is_completed;
    private String completed_on;
    private String config;
    private String[] config_ids;
    private int passed_count;
    private int blocked_count;
    private int untested_count;
    private int retest_count;
    private int failed_count;
    private int custom_status1_count;
    private int custom_status2_count;
    private int custom_status3_count;
    private int custom_status4_count;
    private int custom_status5_count;
    private int custom_status6_count;
    private int custom_status7_count;

    public int getCustom_status1_count()
    {
        return custom_status1_count;
    }

    public void setCustom_status1_count(int custom_status1_count)
    {
        this.custom_status1_count = custom_status1_count;
    }

    public int getCustom_status2_count()
    {
        return custom_status2_count;
    }

    public void setCustom_status2_count(int custom_status2_count)
    {
        this.custom_status2_count = custom_status2_count;
    }

    public int getCustom_status3_count()
    {
        return custom_status3_count;
    }

    public void setCustom_status3_count(int custom_status3_count)
    {
        this.custom_status3_count = custom_status3_count;
    }

    public int getCustom_status4_count()
    {
        return custom_status4_count;
    }

    public void setCustom_status4_count(int custom_status4_count)
    {
        this.custom_status4_count = custom_status4_count;
    }

    public int getCustom_status5_count()
    {
        return custom_status5_count;
    }

    public void setCustom_status5_count(int custom_status5_count)
    {
        this.custom_status5_count = custom_status5_count;
    }

    public int getCustom_status6_count()
    {
        return custom_status6_count;
    }

    public void setCustom_status6_count(int custom_status6_count)
    {
        this.custom_status6_count = custom_status6_count;
    }

    public int getCustom_status7_count()
    {
        return custom_status7_count;
    }

    public void setCustom_status7_count(int custom_status7_count)
    {
        this.custom_status7_count = custom_status7_count;
    }

    private int project_id;
    private int plan_id;
    private String created_on;
    private int created_by;
    private String url;

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

    public int getMilestone_id()
    {
        return milestone_id;
    }

    public void setMilestone_id(int milestone_id)
    {
        this.milestone_id = milestone_id;
    }

    public int getAssignedto_id()
    {
        return assignedto_id;
    }

    public void setAssignedto_id(int assignedto_id)
    {
        this.assignedto_id = assignedto_id;
    }

    public boolean isInclude_all()
    {
        return include_all;
    }

    public void setInclude_all(boolean include_all)
    {
        this.include_all = include_all;
    }

    public boolean isIs_completed()
    {
        return is_completed;
    }

    public void setIs_completed(boolean is_completed)
    {
        this.is_completed = is_completed;
    }

    public String getCompleted_on()
    {
        return completed_on;
    }

    public void setCompleted_on(String completed_on)
    {
        this.completed_on = completed_on;
    }

    public String getConfig()
    {
        return config;
    }

    public void setConfig(String config)
    {
        this.config = config;
    }

    public String[] getConfig_ids()
    {
        return config_ids;
    }

    public void setConfig_ids(String[] config_ids)
    {
        this.config_ids = config_ids;
    }

    public int getPassed_count()
    {
        return passed_count;
    }

    public void setPassed_count(int passed_count)
    {
        this.passed_count = passed_count;
    }

    public int getBlocked_count()
    {
        return blocked_count;
    }

    public void setBlocked_count(int blocked_count)
    {
        this.blocked_count = blocked_count;
    }

    public int getUntested_count()
    {
        return untested_count;
    }

    public void setUntested_count(int untested_count)
    {
        this.untested_count = untested_count;
    }

    public int getRetest_count()
    {
        return retest_count;
    }

    public void setRetest_count(int retest_count)
    {
        this.retest_count = retest_count;
    }

    public int getFailed_count()
    {
        return failed_count;
    }

    public void setFailed_count(int failed_count)
    {
        this.failed_count = failed_count;
    }

    public int getProject_id()
    {
        return project_id;
    }

    public void setProject_id(int project_id)
    {
        this.project_id = project_id;
    }

    public int getPlan_id()
    {
        return plan_id;
    }

    public void setPlan_id(int plan_id)
    {
        this.plan_id = plan_id;
    }

    public String getCreated_on()
    {
        return created_on;
    }

    public void setCreated_on(String created_on)
    {
        this.created_on = created_on;
    }

    public int getCreated_by()
    {
        return created_by;
    }

    public void setCreated_by(int created_by)
    {
        this.created_by = created_by;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

}
