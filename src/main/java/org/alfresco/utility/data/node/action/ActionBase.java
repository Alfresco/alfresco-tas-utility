package org.alfresco.utility.data.node.action;

import org.alfresco.utility.dsl.DSLFile;

public class ActionBase
{
    private DSLFile<?> client;

    public DSLFile<?> getClient()
    {
        return client;
    }

    public void setClient(DSLFile<?> client)
    {
        this.client = client;
    }

}
