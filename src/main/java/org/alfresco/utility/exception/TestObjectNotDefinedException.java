package org.alfresco.utility.exception;

public class TestObjectNotDefinedException extends TestConfigurationException
{

    private static final long serialVersionUID = 1959868878128668760L;

    public TestObjectNotDefinedException(String message)
    {
        super(String.format("Your Test Variable was not initialized properly: %s", message));
    }

}
