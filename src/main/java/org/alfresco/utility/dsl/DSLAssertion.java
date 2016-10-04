package org.alfresco.utility.dsl;

import static org.alfresco.utility.report.log.Step.STEP;

import java.io.File;

public class DSLAssertion<Protocol>
{
    protected DSLProtocol<?> dslProtocol;

    public DSLAssertion(DSLProtocol<?> dslProtocol)
    {
        this.dslProtocol = dslProtocol;
    }

    @SuppressWarnings("unchecked")
    public Protocol getProtocol()
    {
        return (Protocol) dslProtocol;
    }

    @SuppressWarnings("unchecked")
    public Protocol existsInRepo()
    {
        STEP(String.format("CMIS: Assert that content '%s' exists in Repository %s", new File(dslProtocol.getLastResourceWithoutPrefix()).getName(),
                dslProtocol.getLastResourceWithoutPrefix()));
        dslProtocol.dataContent.usingUser(dslProtocol.getTestUser()).assertContentExist(dslProtocol.getLastResourceWithoutPrefix());
        return (Protocol) dslProtocol;
    }

    @SuppressWarnings("unchecked")
    public Protocol doesNotExistInRepo()
    {
        STEP(String.format("CMIS: Assert that content '%s' doesn't exist in repository %s", new File(dslProtocol.getLastResourceWithoutPrefix()).getName(),
                dslProtocol.getLastResourceWithoutPrefix()));
        dslProtocol.dataContent.usingUser(dslProtocol.getTestUser()).assertContentDoesNotExist(dslProtocol.getLastResourceWithoutPrefix());
        return (Protocol) dslProtocol;
    }

}
