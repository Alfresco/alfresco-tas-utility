package org.alfresco.utility.exception;

public class DataPreparationException extends Exception
{
    private static final long serialVersionUID = -8896257049656936132L;

    public DataPreparationException(String message)
    {
        super(String.format("Data Preparation Exception: %s", message));
    }
}
