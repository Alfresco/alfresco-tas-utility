package org.alfresco.utility.exception;

public class EnvironmentConfigurationException extends Exception
{
    private static final long serialVersionUID = -2548744613510433466L;

    public EnvironmentConfigurationException(String message)
    {
        super(String.format("Environment Configuration Probems: %s", message));
    }

}
