package org.alfresco.utility.data;

import static org.alfresco.utility.report.log.Step.STEP;

import java.io.File;
import java.util.Arrays;

import org.alfresco.dataprep.WorkflowService;
import org.alfresco.utility.model.GroupModel;
import org.alfresco.utility.model.TaskModel;
import org.alfresco.utility.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Created by Claudia Agache on 10/11/2016.
 */
@Service
@Scope(value = "prototype")
public class DataWorkflow extends TestData<DataWorkflow>
{
    @Autowired
    WorkflowService workflowService;

    /**
     * Example of usage:
     * dataWorkflow.usingUser(userWhoStartsTask).usingSite(siteModel).usingResource(document).createNewTaskAndAssignTo(assignee);
     * @param userModel
     * @return
     * @throws Exception
     */
    public TaskModel createNewTaskAndAssignTo(UserModel userModel) throws Exception
    {
        return createNewTask(new TaskModel(userModel.getUsername()));
    }

    public TaskModel createNewTask(TaskModel taskModel) throws Exception
    {
        STEP(String.format("DATAPREP: User %s creates new task %s and assigns it to user %s", getCurrentUser().getUsername(), taskModel.getMessage(), taskModel.getAssignee()));

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
    
    /**
     * Starts a Review and Approve(pooled review) workflow with items added from a site
     * Example of usage:
     * dataWorkflow.usingUser(userWhoStartsTask).usingSite(siteModel).usingResource(document).createNewTaskAndAssignTo(assignee);
     * @param userModel
     * @return
     * @throws Exception
     */
    public TaskModel createPooledReviewTaskAndAssignTo(GroupModel groupModel) throws Exception
    {
        TaskModel taskModel = new TaskModel(groupModel.getDisplayName());
        STEP(String.format("DATAPREP: User %s creates new task %s and assigns it to group %s", getCurrentUser().getUsername(), 
                taskModel.getMessage(), taskModel.getAssignee()));

        String workflowId = workflowService.startPooledReview(
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
    
    public TaskModel claimTask(TaskModel taskModel)
    {
        STEP(String.format("DATAPREP: User %s claims task %s", getCurrentUser().getUsername(), taskModel.getMessage()));
        
        workflowService.claimTask(getCurrentUser().getUsername(), getCurrentUser().getPassword(), taskModel.getNodeRef());
        return taskModel;
    }
}
