package org.alfresco.tester.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public abstract class TestModel implements Model
{
    public String toInfo()
    {
        return ToStringBuilder.reflectionToString(this);
    }

    public String toString()
    {
        return toInfo();
    }
}
