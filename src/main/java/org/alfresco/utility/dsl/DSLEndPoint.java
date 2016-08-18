package org.alfresco.utility.dsl;

import org.alfresco.utility.exception.TestConfigurationException;

public interface DSLEndPoint
{
    public String getPrefixSpace();

    public String getCurrentSpace() throws TestConfigurationException;
}
