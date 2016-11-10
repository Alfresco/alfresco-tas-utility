package org.alfresco.utility.model;

import org.testng.Assert;

public class ErrorModel extends TestModel
{
    private String errorKey = "";
    private int statusCode = 0;

    private String briefSummary = "";
    private String stackTrace = "";
    private String descriptionURL = "";
    public static String PERMISSION_WAS_DENIED = "Permission was denied";
    public static String ENTITY_NOT_FOUND = "%s was not found";
    public static String AUTHENTICATION_FAILED = "Authentication failed";

    public String getErrorKey()
    {
        return errorKey;
    }

    public void setErrorKey(String errorKey)
    {
        this.errorKey = errorKey;
    }

    public int getStatusCode()
    {
        return statusCode;
    }

    public void setStatusCode(int statusCode)
    {
        this.statusCode = statusCode;
    }

    public String getBriefSummary()
    {
        return briefSummary;
    }

    public void setBriefSummary(String briefSummary)
    {
        this.briefSummary = briefSummary;
    }

    public String getStackTrace()
    {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace)
    {
        this.stackTrace = stackTrace;
    }

    public String getDescriptionURL()
    {
        return descriptionURL;
    }

    public void setDescriptionURL(String descriptionURL)
    {
        this.descriptionURL = descriptionURL;
    }
    
    public ErrorModel containsSummary(String summary)
    {
        if(!getBriefSummary().contains(summary))
        Assert.fail(String.format("Expected [%s] error to be found in actual briefSummary returned by server: %s", summary, getBriefSummary()));
        
        return this;
    }

}