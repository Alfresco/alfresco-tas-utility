package org.alfresco.utility.dsl;

import static org.alfresco.utility.report.log.Step.STEP;

import org.testng.Assert;

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
        STEP(String.format("CMIS: Assert that content '%s' exists in repository", dslProtocol.getLastResourceWithoutPrefix()));
        dslProtocol.dataContent.usingUser(dslProtocol.getTestUser()).assertContentExist(dslProtocol.getLastResourceWithoutPrefix());
        return (Protocol) dslProtocol;
    }

    @SuppressWarnings("unchecked")
    public Protocol doesNotExistInRepo()
    {
        STEP(String.format("CMIS: Assert that content '%s' doesn't exist in repository", dslProtocol.getLastResourceWithoutPrefix()));
        dslProtocol.dataContent.usingUser(dslProtocol.getTestUser()).assertContentDoesNotExist(dslProtocol.getLastResourceWithoutPrefix());
        return (Protocol) dslProtocol;
    }
    
    /**
     * Just verify using JMX calls if the protocol is enabled on server or not
     */
    public Protocol protocolIsEnabled() throws Exception
    {
        Assert.assertTrue(dslProtocol.isProtocolEnabled(), String.format("%s protocol is enabled", dslProtocol.getProtocolName()));
        return getProtocol();
    }
    

}
