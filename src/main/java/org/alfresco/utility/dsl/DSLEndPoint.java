package org.alfresco.utility.dsl;

import org.alfresco.utility.exception.TestConfigurationException;

public interface DSLEndPoint
{
    public String getRepositoryPrefixPath();

    public String getCurrentRepositorySpace() throws TestConfigurationException;
}
