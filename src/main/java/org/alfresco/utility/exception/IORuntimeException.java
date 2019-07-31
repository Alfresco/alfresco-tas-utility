package org.alfresco.utility.exception;

import java.io.IOException;

/**
 * An unchecked exception to wrap IOExceptions.
 *
 * Usually when a test file cannot be created then we want to fail without trying to handle the exception.
 */
public class IORuntimeException extends RuntimeException
{
    private static final long serialVersionUID = -1037346625487449935L;

    public IORuntimeException(IOException e)
    {
        super(e);
    }
}
