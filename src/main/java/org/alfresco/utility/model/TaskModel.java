package org.alfresco.utility.model;

import org.alfresco.dataprep.CMISUtil.Priority;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskModel extends TestModel
{
    @JsonProperty(value = "description")
    private String message;

    @JsonProperty(value = "dueAt")
    private String dueDate;
    private String assignee;
    private Priority priority;

    public TaskModel()
    {
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getDueDate()
    {
        return dueDate;
    }

    public void setDueDate(String dueDate)
    {
        this.dueDate = dueDate;
    }

    public String getAssignee()
    {
        return assignee;
    }

    public void setAssignee(String assignee)
    {
        this.assignee = assignee;
    }

    public int getPriority()
    {
        return priority.getLevel();
    }

    public void setPriority(Priority priority)
    {
        this.priority = priority;
    }
}
