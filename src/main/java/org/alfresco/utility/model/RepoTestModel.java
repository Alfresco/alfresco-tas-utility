package org.alfresco.utility.model;

import javax.xml.bind.annotation.XmlAttribute;

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
