package org.alfresco.tester.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public abstract class TestModel implements Model
{
    public String toInfo()
    {
        return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
    }

    public String toString()
    {
        return toInfo();
    }
}
