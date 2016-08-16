package org.alfresco.utility.data.node.action;

import org.alfresco.utility.dsl.DSLCrudFile;

public class ActionBase
{
    private DSLCrudFile<?> client;

    public DSLCrudFile<?> getClient()
    {
        return client;
    }

    public void setClient(DSLCrudFile<?> client)
    {
        this.client = client;
    }

}
