package org.alfresco.utility.data;

import org.alfresco.dataprep.WorkflowService;
import org.alfresco.utility.model.TaskModel;
import org.alfresco.utility.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.testng.Assert;

import java.io.File;
import java.util.Arrays;

/**
 * Created by Claudia Agache on 10/11/2016.
 */
@Service
@Scope(value = "prototype")
public class DataWorkflow extends TestData<DataWorkflow>
{
    @Autowired
    WorkflowService workflowService;

    public TaskModel createNewTaskAndAssignTo(UserModel userModel) throws Exception
    {
        return createNewTask(new TaskModel(userModel.getUsername()));
    }

    public TaskModel createNewTask(TaskModel taskModel) throws Exception
    {
        LOG.info("User {} creates new task {} and assigns it to user {}", getCurrentUser().getUsername(), taskModel.getMessage(), taskModel.getAssignee());

        String workflowId = workflowService.startNewTask(
                getCurrentUser().getUsername(),
                getCurrentUser().getPassword(),
                taskModel.getMessage(),
                taskModel.getDueDate(),
                taskModel.getAssignee(),
                taskModel.getPriority(),
                getCurrentSite(),
                Arrays.asList(new File(getLastResource()).getName()),
                taskModel.getSendEmail()
                );
        taskModel.setNodeRef(workflowId);
        return taskModel;
    }
}
