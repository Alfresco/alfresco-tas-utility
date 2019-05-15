package org.alfresco.utility.network;

public interface Jmx
{
    Object writeProperty(String objectName, String attributeName, Object attributeValue) throws Exception;

    Object readProperty(String objectName, String attributeName) throws Exception;

    public boolean isJMXEnabled();

    Object executeJMXMethod(String objectName, String methodName, Object ... pArgs) throws Exception;
}
