package org.alfresco.utility.report.json;

import java.io.File;
import java.nio.file.Paths;

import org.alfresco.utility.Utility;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.testng.ITestContext;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonSuite
{
    private String name;
    private JsonClass testClass;
    private String jsonPathFile;
    private String startedAt;
    private String finishedAt;

    public JsonSuite(ITestContext context, String jsonPathFile)
    {
        this.jsonPathFile = jsonPathFile;
        setName(context.getCurrentXmlTest().getSuite().getName());
        setStartedAt(context.getStartDate().toString());
        setTestClass(new JsonClass(context.getCurrentXmlTest().getClasses().get(0).getName()));
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public JsonClass getTestClass()
    {
        return testClass;
    }

    public void setTestClass(JsonClass testClass)
    {
        this.testClass = testClass;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public String getStartedAt()
    {
        return startedAt;
    }

    public void setStartedAt(String startedAt)
    {
        this.startedAt = startedAt;
    }

    public String getFinishedAt()
    {
        return finishedAt;
    }

    public void setFinishedAt(String finishedAt)
    {
        this.finishedAt = finishedAt;
    }

    public void writeToDisk() throws Exception
    {
        Utility.checkObjectIsInitialized(jsonPathFile, "jsonPathFile");
        ObjectMapper mapper = new ObjectMapper();
                         
        File out = Paths.get(jsonPathFile, String.format("%s-%s.json", getName(),System.currentTimeMillis() )).toFile();

        mapper.writeValue(out, this);

    }
}
