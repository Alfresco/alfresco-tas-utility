package org.alfresco.utility.constants;

public enum ActivityType
{
    USER_JOINED("org.alfresco.site.user-joined"),
    USER_LEFT("org.alfresco.site.user-left"),
    USER_ROLE_CHANGED("org.alfresco.site.user-role-changed"),
    FILE_ADDED("org.alfresco.documentlibrary.file-added"),
    FILE_PREVIEWD("org.alfresco.documentlibrary.file-previewed"),
    FILE_DELETED("org.alfresco.documentlibrary.file-deleted"),
    FILE_CREATED("org.alfresco.comments.comment-created"),
    FILE_LIKED("org.alfresco.documentlibrary.file-liked"),
    FOLDER_ADDED("org.alfresco.documentlibrary.folder-added"),
    COMMENT_CREATED("org.alfresco.comments.comment-created"),
    COMMENT_UPDATED("org.alfresco.comments.comment-updated"),
    COMMENT_DELETED("org.alfresco.comments.comment-deleted");

    private final String activityType;

    private ActivityType(final String activityType)
    {
        this.activityType = activityType;
    }

    @Override
    public String toString()
    {
        return activityType;
    }
}
