package org.alfresco.utility.testrail.model;

import java.util.List;

/**
 * "id": 545927,
 * "case_id": 87292,
 * "status_id": 3,
 * "assignedto_id": null,
 * "run_id": 147,
 * "title": "one",
 * "template_id": 1,
 * "type_id": 6,
 * "priority_id": 2,
 * "estimate": null,
 * "estimate_forecast": null,
 * "refs": null,
 * "milestone_id": null,
 * "custom_executiontype": true,
 * "custom_auto_ref": "org.alfresco.utility.testrail.RunTestRailIntegrationTest#one",
 * "custom_description": "My Awesome test 100",
 * "custom_test_notes": null,
 * "custom_preconds": null,
 * "custom_expected": null,
 * "custom_exce_type": [
 * 3
 * ],
 * "custom_min_vers": []
 */
public class RunTestCase
{
    private long id;
    private long case_id;
    private int status_id;
    private String assignedto_id;
    private int run_id;
    private String title;
    private int template_id;
    private int type_id;
    private int priority_id;
    private String estimate;
    private String estimate_forecast;
    private String refs;
    private String milestone_id;
    private String custom_executiontype;
    private int custom_automation_type;
    private String custom_auto_ref;
    private String custom_description;
    private String custom_test_notes;
    private String custom_preconds;
    private String custom_expected;
    private List<String> custom_exce_type;
    private List<String> custom_min_vers;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getCase_id()
    {
        return case_id;
    }

    public void setCase_id(long case_id)
    {
        this.case_id = case_id;
    }

    public int getStatus_id()
    {
        return status_id;
    }

    public void setStatus_id(int status_id)
    {
        this.status_id = status_id;
    }

    public String getAssignedto_id()
    {
        return assignedto_id;
    }

    public void setAssignedto_id(String assignedto_id)
    {
        this.assignedto_id = assignedto_id;
    }

    public int getRun_id()
    {
        return run_id;
    }

    public void setRun_id(int run_id)
    {
        this.run_id = run_id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
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

    public String getRefs()
    {
        return refs;
    }

    public void setRefs(String refs)
    {
        this.refs = refs;
    }

    public String getMilestone_id()
    {
        return milestone_id;
    }

    public void setMilestone_id(String milestone_id)
    {
        this.milestone_id = milestone_id;
    }

    public String getCustom_executiontype()
    {
        return custom_executiontype;
    }

    public void setCustom_executiontype(String custom_executiontype)
    {
        this.custom_executiontype = custom_executiontype;
    }
    
    public int getCustom_automation_type()
    {
        return custom_automation_type;
    }
    
    public void setCustom_automation_type(int custom_automation_type)
    {
        this.custom_automation_type = custom_automation_type;
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

    public String getCustom_preconds()
    {
        return custom_preconds;
    }

    public void setCustom_preconds(String custom_preconds)
    {
        this.custom_preconds = custom_preconds;
    }

    public String getCustom_expected()
    {
        return custom_expected;
    }

    public void setCustom_expected(String custom_expected)
    {
        this.custom_expected = custom_expected;
    }

    public List<String> getCustom_exce_type()
    {
        return custom_exce_type;
    }

    public void setCustom_exce_type(List<String> custom_exce_type)
    {
        this.custom_exce_type = custom_exce_type;
    }

    public List<String> getCustom_min_vers()
    {
        return custom_min_vers;
    }

    public void setCustom_min_vers(List<String> custom_min_vers)
    {
        this.custom_min_vers = custom_min_vers;
    }
}
