package org.alfresco.tester.exception;

public class JmxException extends Exception
{
    private static final long serialVersionUID = 1L;

    public JmxException(String message)
    {
        super(String.format("JMX connection cannot be established: %s", message));
    }
}
