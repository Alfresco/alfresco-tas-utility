package org.alfresco.utility.report.json;

import java.util.ArrayList;
import java.util.List;

import org.testng.ITestResult;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonClass
{
    private List<JsonTest> tests = new ArrayList<JsonTest>();

    @JsonProperty(value = "className")
    private String name;

    public JsonClass(String name)
    {
        setName(name);
    }

    public List<JsonTest> getTests()
    {
        return tests;
    }

    public void setTests(List<JsonTest> jsonTests)
    {
        this.tests = jsonTests;
    }

    public void addTest(ITestResult result)
    {
        getTests().add(new JsonTest(result));
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
