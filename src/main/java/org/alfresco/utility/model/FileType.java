package org.alfresco.utility.model;

import java.util.stream.Stream;

import com.google.common.io.Files;

import org.alfresco.dataprep.CMISUtil.DocumentType;
import org.alfresco.utility.data.DataValue;

public enum FileType
{
    UNDEFINED("N#A", DataValue.UNDEFINED.toString()),
    EXE("N#A", "exe"),
    TEXT_PLAIN("text/plain", "txt"),
    XML("text/xml", "xml"),
    HTML("text/html", "html"),
    PDF("application/pdf", "pdf"),
    MSWORD("application/msword", "doc"),
    MSWORD2007("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx"),
    MSEXCEL("application/vnd.ms-excel", "xls"),
    MSEXCEL2007("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx"),
    MSPOWERPOINT("application/vnd.ms-powerpoint", "ppt"),
    MSPOWERPOINT2007("application/vnd.openxmlformats-officedocument.presentationml.presentation", "pptx");

    public final String mimeType;
    public final String extension;

    FileType(String mimeType, String extension)
    {
        this.mimeType = mimeType;
        this.extension = extension;
    }

    public static FileType fromName(String fileName)
    {
        String extension = Files.getFileExtension(fileName);

        for (FileType ft : FileType.values())
        {
            if (ft.extension.equals(extension))
                return ft;
        }

        return FileType.UNDEFINED;
    }

    /**
     * Try to find a DataPrep {@link DocumentType} that is equivalent to this FileType value.
     *
     * @return The DocumentType, or null if no equivalent type could be found.
     */
    public DocumentType getDocumentType()
    {
        // Try to find a DocumentType with a matching MIME type.
        return Stream.of(DocumentType.values())
                     .filter(documentType -> documentType.type.equals(this.mimeType))
                     .findFirst()
                     // If that fails then try to match the extension.
                     .orElse(Stream.of(DocumentType.values())
                                   .filter(documentType -> documentType.extention.equals(this.extension))
                                   .findFirst()
                                   // If that fails then return null.
                                   .orElse(null));
    }
}