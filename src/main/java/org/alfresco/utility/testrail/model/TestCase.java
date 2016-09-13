package org.alfresco.utility.testrail.model;

import java.util.List;

/**
 * Represents one Test Case object from Test Rail
 * {
 * "id": 10,
 * "title": "adminShouldCreateComments",
 * "section_id": 2,
 * "template_id": 1,
 * "type_id": 3,
 * "priority_id": 2,
 * "milestone_id": null,
 * "refs": null,
 * "created_by": 1,
 * "created_on": 1472905188,
 * "updated_by": 1,
 * "updated_on": 1472905188,
 * "estimate": null,
 * "estimate_forecast": null,
 * "suite_id": 1,
 * "custom_preconds": null,
 * "custom_steps": null,
 * "custom_expected": null,
 * "custom_steps_separated": null,
 * "custom_mission": null,
 * "custom_goals": null
 * }
 */
public class TestCase
{
    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public int getSection_id()
    {
        return section_id;
    }

    public void setSection_id(int section_id)
    {
        this.section_id = section_id;
    }

    public int getTemplate_id()
    {
        return template_id;
    }

    public void setTemplate_id(int template_id)
    {
        this.template_id = template_id;
    }

    public int getType_id()
    {
        return type_id;
    }

    public void setType_id(int type_id)
    {
        this.type_id = type_id;
    }

    public int getPriority_id()
    {
        return priority_id;
    }

    public void setPriority_id(int priority_id)
    {
        this.priority_id = priority_id;
    }

    public int getMilestone_id()
    {
        return milestone_id;
    }

    public void setMilestone_id(int milestone_id)
    {
        this.milestone_id = milestone_id;
    }

    public String getRefs()
    {
        return refs;
    }

    public void setRefs(String refs)
    {
        this.refs = refs;
    }

    public int getCreated_by()
    {
        return created_by;
    }

    public void setCreated_by(int created_by)
    {
        this.created_by = created_by;
    }

    public String getCreated_on()
    {
        return created_on;
    }

    public void setCreated_on(String created_on)
    {
        this.created_on = created_on;
    }

    public String getUpdated_by()
    {
        return updated_by;
    }

    public void setUpdated_by(String updated_by)
    {
        this.updated_by = updated_by;
    }

    public String getUpdated_on()
    {
        return updated_on;
    }

    public void setUpdated_on(String updated_on)
    {
        this.updated_on = updated_on;
    }

    public String getEstimate()
    {
        return estimate;
    }

    public void setEstimate(String estimate)
    {
        this.estimate = estimate;
    }

    public String getEstimate_forecast()
    {
        return estimate_forecast;
    }

    public void setEstimate_forecast(String estimate_forecast)
    {
        this.estimate_forecast = estimate_forecast;
    }

    public int getSuite_id()
    {
        return suite_id;
    }

    public void setSuite_id(int suite_id)
    {
        this.suite_id = suite_id;
    }

    public String getCustom_preconds()
    {
        return custom_preconds;
    }

    public void setCustom_preconds(String custom_preconds)
    {
        this.custom_preconds = custom_preconds;
    }

    public String getCustom_steps()
    {
        return custom_steps;
    }

    public void setCustom_steps(String custom_steps)
    {
        this.custom_steps = custom_steps;
    }

    public String getCustom_expected()
    {
        return custom_expected;
    }

    public void setCustom_expected(String custom_expected)
    {
        this.custom_expected = custom_expected;
    }

    public String getCustom_steps_separated()
    {
        return custom_steps_separated;
    }

    public void setCustom_steps_separated(String custom_steps_separated)
    {
        this.custom_steps_separated = custom_steps_separated;
    }

    public String getCustom_mission()
    {
        return custom_mission;
    }

    public void setCustom_mission(String custom_mission)
    {
        this.custom_mission = custom_mission;
    }

    public String getCustom_goals()
    {
        return custom_goals;
    }

    public void setCustom_goals(String custom_goals)
    {
        this.custom_goals = custom_goals;
    }

    public List<Integer> getCustom_exce_type()
    {
        return custom_exce_type;
    }

    public void setCustom_exce_type(List<Integer> custom_exce_type)
    {
        this.custom_exce_type = custom_exce_type;
    }

    public boolean isCustom_executiontype()
    {
        return custom_executiontype;
    }

    public void setCustom_executiontype(boolean custom_executiontype)
    {
        this.custom_executiontype = custom_executiontype;
    }

    

    public String getCustom_auto_ref()
    {
        return custom_auto_ref;
    }

    public void setCustom_auto_ref(String custom_auto_ref)
    {
        this.custom_auto_ref = custom_auto_ref;
    }

    public String getCustom_description()
    {
        return custom_description;
    }

    public void setCustom_description(String custom_description)
    {
        this.custom_description = custom_description;
    }

    public String getCustom_test_notes()
    {
        return custom_test_notes;
    }

    public void setCustom_test_notes(String custom_test_notes)
    {
        this.custom_test_notes = custom_test_notes;
    }

    private int id;
    private String title;
    private int section_id;
    private int template_id;
    private int type_id;
    private int priority_id;
    private int milestone_id;
    private String refs;
    private int created_by;
    private String created_on;
    private String updated_by;
    private String updated_on;
    private String estimate;
    private String estimate_forecast;
    private int suite_id;
    private String custom_preconds;
    private String custom_steps;
    private String custom_expected;
    private String custom_steps_separated;
    private String custom_mission;
    private String custom_goals;
    private List<Integer> custom_exce_type;
    private boolean custom_executiontype;
    private String custom_auto_ref;
    private String custom_description;
    private String custom_test_notes;
}
