package org.alfresco.utility.model;

import javax.xml.bind.annotation.XmlAttribute;

import org.alfresco.utility.Utility;

/**
 * This is a model from repo that has the following fields: nodeRef and identifier
 */
public abstract class RepoTestModel extends TestModel
{
    private String nodeRef;
    private int identifier;

    public String getNodeRef()
    {
        return nodeRef;
    }
    
    /**
     * Example: 25eb5ef8-50a5-4873-adc5-8edd87e628a4;1.0
     * will be returned as "25eb5ef8-50a5-4873-adc5-8edd87e628a4" without 1.0
     * @return {@link #getGuid() } without version number
     */    
    public String getNodeRefWithoutVersion()
    {
        return Utility.splitGuidVersion(getNodeRef());
    }

    public void setNodeRef(String nodeRef)
    {
        this.nodeRef = nodeRef;
    }

    @XmlAttribute(name = "id", required = true)
    public int getIdentifier()
    {
        return identifier;
    }

    public void setIdentifier(int identifier)
    {
        this.identifier = identifier;
    }
}
