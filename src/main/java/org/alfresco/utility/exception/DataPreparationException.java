package org.alfresco.utility.exception;

public class DataPreparationException extends RuntimeException
{
    private static final long serialVersionUID = -8896257049656936132L;

    public DataPreparationException(String message)
    {
        super(String.format("Errors during test data preparation: %s", message));
    }

    public DataPreparationException(Exception e)
    {
        super("Errors during test data preparation", e);
    }
}
