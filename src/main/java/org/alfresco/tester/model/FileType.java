package org.alfresco.tester.model;

public enum FileType
{
    TEXT_PLAIN("text/plain"),
    XML("text/xml"),
    HTML("text/html"),
    PDF("application/pdf"),
    MSWORD("application/msword"),
    MSEXCEL("application/vnd.ms-excel"),
    MSPOWERPOINT("application/vnd.ms-powerpoint");
    public final String type;
    FileType(String type)
    {
        this.type = type;
    }
}
