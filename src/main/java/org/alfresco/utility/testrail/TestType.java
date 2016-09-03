package org.alfresco.utility.testrail;

public enum TestType
{
    AUTOMATED(3), FUNCTIONAL(6), OTHER(7), REGRESSION(9);

    private int value;

    private TestType(int value)
    {
        this.value = value;
    }

   
    public Object value()
    {       
        return new Integer(this.value);
    }
}
