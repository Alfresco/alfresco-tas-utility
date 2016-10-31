package org.alfresco.utility.model;

import org.alfresco.utility.data.DataValue;

import com.google.common.io.Files;

public enum FileType
{
    UNDEFINED("N#A", DataValue.UNDEFINED.toString()),
    EXE("N#A", "exe"),
    TEXT_PLAIN("text/plain", "txt"),
    XML("text/xml", "xml"),
    HTML("text/html", "html"),
    PDF("application/pdf", "pdf"),
    MSWORD("application/msword", "doc"),
    MSWORD2003("application/msword", "doc"),
    MSWORD2007("application/msword", "docx"),
    MSEXCEL("application/vnd.ms-excel", "xls"),
    MSEXCEL2003("application/vnd.ms-excel", "xls"),
    MSEXCEL2007("application/vnd.ms-excel", "xlsx"),
    MSPOWERPOINT("application/vnd.ms-powerpoint", "ppt"),
    MSPOWERPOINT2003("application/vnd.ms-powerpoint", "ppt"),
    MSPOWERPOINT2007("application/vnd.ms-powerpoint", "pptx");

    public final String mimeType;
    public final String extention;

    FileType(String mimeType, String extention)
    {
        this.mimeType = mimeType;
        this.extention = extention;
    }

    public static FileType fromName(String fileName)
    {
        String extention = Files.getFileExtension(fileName);

        for (FileType ft : FileType.values())
        {
            if (ft.extention.equals(extention))
                return ft;
        }

        return FileType.UNDEFINED;
    }
}
