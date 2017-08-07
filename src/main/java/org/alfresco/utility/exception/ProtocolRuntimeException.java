package org.alfresco.utility.exception;

public class ProtocolRuntimeException extends RuntimeException
{
    private static final long serialVersionUID = -8284196521431424833L;

    public ProtocolRuntimeException(String message)
    {
        super(String.format("Exception thrown when creating content: %s}", message));
    }

}
