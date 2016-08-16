package org.alfresco.utility.exception;

public class TestConfigurationException extends Exception
{
    private static final long serialVersionUID = 1562196340548867033L;

    public TestConfigurationException(String message)
    {
        super(String.format("You missed some configuration settings in your tests: %s", message));
    }
}
