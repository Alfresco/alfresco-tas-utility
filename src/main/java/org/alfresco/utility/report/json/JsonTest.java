package org.alfresco.utility.report.json;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.testng.ITestResult;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonTest
{
    private String name;
    private String startedAt;
    private String finishedAt;
    private String description;
    private String exception;
    private String status;
    private List<String> groups = new ArrayList<String>();

    @JsonProperty(value = "isConfig")
    private boolean isConfig;

    public JsonTest(ITestResult result)
    {
        setName(result.getMethod().getMethodName());
        setStartedAt(result.getStartMillis());
        setFinishedAt(result.getEndMillis());
        setStatus(getHumanReadableStatus(result.getStatus()));
        setGroups(Arrays.asList(result.getMethod().getGroups()));
        
        if (!result.isSuccess())
        {
            if (result.getThrowable() != null)
            {
                if (result.getThrowable().getStackTrace() != null)
                {
                    StringWriter sw = new StringWriter();
                    result.getThrowable().printStackTrace(new PrintWriter(sw));
                    setException(sw.toString());
                }
            }
        }
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getStartedAt()
    {
        return startedAt;
    }

    public void setStartedAt(long startedAt)
    {
        this.startedAt = dateFormat(startedAt);
    }

    public String getFinishedAt()
    {
        return finishedAt;
    }

    public void setFinishedAt(long finishedAt)
    {
        this.finishedAt = dateFormat(finishedAt);
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getException()
    {
        return exception;
    }

    public void setException(String exception)
    {
        this.exception = exception;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    private String dateFormat(long miliseconds)
    {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(miliseconds);
    }

    private String getHumanReadableStatus(int value)
    {
        String status = "";
        switch (value)
        {
            case ITestResult.SUCCESS:
                status = "PASSED";
                break;
            case ITestResult.FAILURE:
                status = "FAILED";
                break;
            case ITestResult.SKIP:
                status = "SKIPPED";
                break;
            case ITestResult.SUCCESS_PERCENTAGE_FAILURE:
                status = "PASSED";
                break;
            default:
                break;
        }
        return status;
    }

    public List<String> getGroups()
    {
        return groups;
    }

    public void setGroups(List<String> groups)
    {
        this.groups = groups;
    }

}
