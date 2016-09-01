package org.alfresco.utility.model;

import org.alfresco.utility.LogFactory;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;

public abstract class TestModel implements Model
{
    private String nodeRef;
    static Logger LOG = LogFactory.getLogger();
    
    public String toInfo()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public String toString()
    {
        return String.format("\n%s", toInfo());
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
