package org.alfresco.utility.exception;

public class ProtocolRuntimeException extends Exception
{
    private static final long serialVersionUID = -8284196521431424833L;

    public ProtocolRuntimeException(String message)
    {
        super(String.format("Exception thrown when creating content: P%s}", message));
    }

}
