package org.alfresco.utility.testrail;

/**
 * Default TestType as defined in our Test Rail Management System.
 */
public enum TestType
{
    ACCESSIBILITY(2), COMPATIBILITY(4), DESTRUCTIVE(5), FUNCTIONAL(6), PERFORMANCE(8), OTHER(7), SECURITY(10), USABILITY(12);

    private int value;

    private TestType(int value)
    {
        this.value = value;
    }

    public int value()
    {
        return Integer.valueOf(this.value);
    }
}
