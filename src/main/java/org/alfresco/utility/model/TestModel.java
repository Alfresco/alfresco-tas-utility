package org.alfresco.utility.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public abstract class TestModel implements Model
{
    private String nodeRef;

    public String toInfo()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public String toString()
    {
        return toInfo();
    }

    public String getNodeRef()
    {
        return nodeRef;
    }

    public void setNodeRef(String nodeRef)
    {
        this.nodeRef = nodeRef;
    }
}