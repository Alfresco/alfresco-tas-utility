package org.alfresco.utility.testrail;

public enum ExecutionType
{
    /*
     * This are currently Setup in alfresco.testrail.net
     * 1, Acceptance
     * 2, Sanity
     * 3, Smoke
     * 4, Regression
     */
    ACCEPTANCE(1), SANITY(2), SMOKE(3), REGRESSION(4);

    private int value;

    private ExecutionType(int value)
    {
        this.value = value;
    }

    public int value()
    {
        return value;
    }

}
