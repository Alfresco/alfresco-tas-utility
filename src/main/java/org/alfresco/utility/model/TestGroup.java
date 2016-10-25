package org.alfresco.utility.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface TestGroup
{
    public static String SANITY = "sanity";
    public static String PROTOCOLS = "protocols";

    /*
     * Used in Rest api mostly
     */
    public static String REST_API = "rest-api";
    public static String COMMENTS = "comments";
    public static String DEPLOYMENTS = "deployments";
    public static String FAVORITES = "favorites";
    public static String NETWORKS = "networks";
    public static String PREFERENCES = "preferences";
    public static String PROCESSES = "processes";
    public static String WORKFLOW = "workflow";
    public static String PEOPLE = "people";
    public static String RATINGS = "ratings";
    public static String SITES = "sites";
    public static String TAGS = "tags";
    public static String TASKS = "tasks";
    public static String PROCESS_DEFINITION = "process-definitions";
    public static String ACTIVITIES = "activities";

    public static String IMAP = "imap";
    public static String SMTP = "smtp";
    public static String CIFS = "cifs";
    public static String CMIS = "cmis";
    public static String WEBDAV = "webdav";
    public static String FTP = "ftp";
    public static String INTEGRATION = "integration";
}
