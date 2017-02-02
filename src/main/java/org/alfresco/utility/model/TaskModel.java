package org.alfresco.utility.model;

import java.util.Date;

import org.alfresco.dataprep.CMISUtil.Priority;
import org.alfresco.utility.data.RandomData;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskModel extends TestModel
{
    @JsonProperty(required = true)
    private String id;

    @JsonProperty(value = "description")
    private String message;

    @JsonProperty(value = "dueAt")
    private Date dueDate;
    private String assignee;
    private Priority priority;
    private Boolean sendEmail;
    
    private String processId;

    public TaskModel()
    {
    }

    public TaskModel(String assignee)
    {
        setAssignee(assignee);
        setMessage(RandomData.getRandomAlphanumeric());
        DateTime today = new DateTime();
        setDueDate(today.plusDays(2).toDate());
        setPriority(Priority.High);
        setSendEmail(true);
    }
    
    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public Date getDueDate()
    {
        return dueDate;
    }

    public void setDueDate(Date dueDate)
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

    public Priority getPriority()
    {
        return priority;
    }

    public void setPriority(Priority priority)
    {
        this.priority = priority;
    }

    public Boolean getSendEmail()
    {
        return sendEmail;
    }

    public void setSendEmail(Boolean sendEmail)
    {
        this.sendEmail = sendEmail;
    }
    

    public String getProcessId()
    {
        return processId;
    }

    public void setProcessId(String processId)
    {
        this.processId = processId;
    }
}
