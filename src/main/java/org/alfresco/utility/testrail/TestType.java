package org.alfresco.utility.testrail;

public enum TestType
{
    FUNCTIONAL(6), OTHER(7);

    private int value;

    private TestType(int value)
    {
        this.value = value;
    }

    public int value()
    {
        return new Integer(this.value);
    }
}
