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
    public static String RELATIONSHIP_NOT_FOUND = "The relationship resource was not found for the entity with id: %s and a relationship id of %s";
    public static String AUTHENTICATION_FAILED = "Authentication failed";
    public static String INVALID_ARGUMENT = "An invalid %s was received";
    public static String UNABLE_TO_LOCATE = "Unable to locate resource";
    public static String NULL_ARGUMENT = "Must provide a non-null %s";
    public static String CANNOT_COMMENT = "Cannot comment on this node";
    public static String INVALID_RATING = "Invalid ratingSchemeId %s";
    public static String NO_CONTENT = "Could not read content from HTTP request body: %s";
    public static String NULL_LIKE_RATING = "Rating should be non-null and a boolean for 'likes' rating scheme";
    public static String NULL_FIVESTAR_RATING = "Rating should be non-null and an integer for 'fiveStar' rating scheme";
    public static String CANNOT_RATE = "Cannot rate this node";

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