package org.alfresco.utility.data.node.action;

import org.alfresco.utility.dsl.DSLCrud;

public class ActionBase
{
    private DSLCrud<?> client;

    public DSLCrud<?> getClient()
    {
        return client;
    }

    public void setClient(DSLCrud<?> client)
    {
        this.client = client;
    }

}
