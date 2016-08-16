package org.alfresco.utility;

import org.alfresco.utility.exception.TestObjectNotDefinedException;

public class Utility
{

    public static void checkObjectIsInitialized(Object model, String message) throws Exception
    {
        if (model == null)
            throw new TestObjectNotDefinedException(message);
    }
}
