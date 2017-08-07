package org.alfresco.utility.exception;

public class ReportConfigurationException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public ReportConfigurationException(String message)
    {
        super(String.format("Report configuration settings could not be loaded: %s", message));
    }
}
