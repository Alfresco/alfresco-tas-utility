package org.alfresco.utility.testrail.model;

import java.util.List;

/**
 * The model compatible with TestRail v5.7.0.3951
 * 
 * "id": 6457024,
    "case_id": 309025,
    "status_id": 5,
    "assignedto_id": null,
    "run_id": 749,
    "title": "getDefaultRepositoryInstalledModules",
    "template_id": 1,
    "type_id": 6,
    "priority_id": 2,
    "estimate": null,
    "estimate_forecast": null,
    "refs": null,
    "milestone_id": null,
    "custom_executiontype": true,
    "custom_platform": 1,
    "custom_automation_type": null,
    "custom_testlink_id": null,
    "custom_auto_ref": "org.alfresco.rest.discovery.DiscoveryTests#getDefaultRepositoryInstalledModules",
    "custom_description": "Sanity tests for GET /discovery endpoint",
    "custom_test_notes": "\n\nTestCase Steps\n=========\n* REST API: Basic Authentication using user {\norg.alfresco.utility.model.UserModel@363751f2[\r\n  username=User-FOEUSgiRWEkvBHC\r\n  password=password\r\n  domain=<null>\r\n  firstName=<null>\r\n  lastName=<null>\r\n  userRole=<null>\r\n]}\n* Request: GET http://localhost:8084/alfresco/api/discovery/\n\n* REST API: Assert that status code is 200",
    "custom_preconds": null,
    "custom_expected": null,
    "custom_steps_separated": null,
    "custom_exce_type": [
        2
    ],
    "custom_max_version": [],
    "custom_min_vers": []
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
    private int custom_platform;
    private int custom_automation_type;
    private String custom_testlink_id;
    private String custom_auto_ref;
    private String custom_description;
    private String custom_test_notes;
    private String custom_preconds;
    private String custom_expected;
    private List<CustomStepsSeparated> custom_steps_separated;
    private List<String> custom_exce_type;
    private List<String> custom_max_version;
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
    
    public int getCustom_platform() {
		return custom_platform;
	}

	public void setCustom_platform(int custom_platform) {
		this.custom_platform = custom_platform;
	}

	public String getCustom_testlink_id() {
		return custom_testlink_id;
	}

	public void setCustom_testlink_id(String custom_testlink_id) {
		this.custom_testlink_id = custom_testlink_id;
	}

	public List<CustomStepsSeparated> getCustom_steps_separated() {
		return custom_steps_separated;
	}

	public void setCustom_steps_separated(List<CustomStepsSeparated> custom_steps_separated) {
		this.custom_steps_separated = custom_steps_separated;
	}

	public List<String> getCustom_max_version() {
		return custom_max_version;
	}

	public void setCustom_max_version(List<String> custom_max_version) {
		this.custom_max_version = custom_max_version;
	}
}
