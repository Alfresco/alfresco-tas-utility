package org.alfresco.utility.exception;

public class JmxException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public JmxException(String message)
    {
        super(String.format("JMX exception encountered with the following message: %s", message));
    }
}