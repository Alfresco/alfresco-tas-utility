package org.alfresco.utility.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author Cristina Axinte
 *
 */
public class ProcessModel extends TestModel
{
    @JsonProperty(required = true)
    private String id;
    
    private List<TaskModel> tasks = null;
    
    public List<TaskModel> getTasks()
    {
        return tasks= new ArrayList<TaskModel>();
    }

    public void addTaskToList(TaskModel task)
    {
        if(tasks == null)
        {
            tasks= new ArrayList<TaskModel>();
        }
        this.tasks.add(task);
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }
    
    public TaskModel getTaskOfUser(UserModel user)
    {
        for(TaskModel task : tasks)
        {
            if(task.getAssignee().equals(user.getUsername()))
            {
                return task;
            }
        }
        
        return null;
    }
    
    
}
