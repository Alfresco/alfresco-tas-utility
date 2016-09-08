package org.alfresco.utility.exception;

public class XMLToModelUnmarshalException extends Exception
{
    
    private static final long serialVersionUID = 1L;

    public <T> XMLToModelUnmarshalException(Class<T> classz, Exception e)
    {
        super(String.format("Could not parse XML Response to model [%s] error: %s", classz.getName(), e.getMessage()));
    }

}
