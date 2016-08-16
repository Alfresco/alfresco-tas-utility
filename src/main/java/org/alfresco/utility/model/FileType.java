package org.alfresco.utility.model;

public enum FileType
{
    TEXT_PLAIN("text/plain", ".txt"),
    XML("text/xml", ".xml"),
    HTML("text/html", ".html"),
    PDF("application/pdf", ".pdf"),
    MSWORD("application/msword", ".doc"),
    MSEXCEL("application/vnd.ms-excel", ".xls"),
    MSPOWERPOINT("application/vnd.ms-powerpoint", ".ppt");
    
    public final String mimeType;
    public final String extention;

    FileType(String mimeType, String extention)
    {
        this.mimeType = mimeType;
        this.extention = extention;
    }

}
