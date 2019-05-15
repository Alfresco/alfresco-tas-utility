package org.alfresco.utility.exception;

public class TestCaseNotFoundException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public TestCaseNotFoundException(String message)
    {
        super(String.format("TestCase not found: %s", message));
    }

}
