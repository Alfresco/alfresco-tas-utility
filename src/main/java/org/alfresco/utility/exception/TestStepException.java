package org.alfresco.utility.exception;

public class TestStepException extends RuntimeException
{
    private static final long serialVersionUID = -3444109891687878293L;

    public TestStepException(String expected, String actual)
    {
        super(String.format("EXPECTED: %s. ACTUAL: %s", expected, actual));
    }

}
