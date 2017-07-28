package org.alfresco.utility.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface TestGroup
{
    String SANITY = "sanity";
    String CORE = "core";
    String FULL = "full";
    String PROTOCOLS = "protocols";
    String SSO = "sso";

    /*
     * Used in Rest api mostly
     */
    String REST_API = "rest-api";
    String COMMENTS = "comments";
    String DEPLOYMENTS = "deployments";
    String FAVORITES = "favorites";
    String NETWORKS = "networks";
    String PREFERENCES = "preferences";
    String PROCESSES = "processes";
    String WORKFLOW = "workflow";
    String PEOPLE = "people";
    String RATINGS = "ratings";
    String SITES = "sites";
    String TAGS = "tags";
    String TASKS = "tasks";
    String PROCESS_DEFINITION = "process-definitions";
    String ACTIVITIES = "activities";
    String EXTENTION_POINTS = "extention-points";
    String NODES = "nodes";
    String AUTH = "auth";
    String SEARCH = "search";
    String RENDITIONS = "renditions";
    String AUDIT = "audit";
    String IMAP = "imap";
    String SMTP = "smtp";
    String CIFS = "cifs";
    String CMIS = "cmis";
    String QUERIES = "queries";
    String WEBDAV = "webdav";
    String FTP = "ftp";
    String AOS = "aos";
    String INTEGRATION = "integration";
    String CONTENT = "content";
    String PREUPGRADE = "pre-upgrade";
    String POSTUPGRADE = "post-upgrade";
    String SHARE = "share";
    String BROWSER = "browser";
    String SECURITY = "security";

    String WONT_FIX = "wont-fix";
    String OS_WIN = "windows"; // this will mark tests that needs to be executed ONLY on Windows
    String OS_LINUX = "linux"; // this will mark tests that needs to be executed ONLY on Unix
    String OS_MAC = "mac"; // this will mark tests that needs to be executed ONLY on Unix
    String ASS_1 = "ASS_1.0.0"; // Alfresco Search Services 1.0

    /*
     * Used in Share-PO
     */
    String SITES_FEATURES = "sites-features";
    String ADMIN_TOOLS = "admin-tools";
    String USER_DASHBOARD = "user-dashboard";
    String GOOGLE_DOCS = "google-docs";
    String USER = "user";
    String MANAGE_INVITES = "manage-invites";

    /*
     * Used in Admin Console
     */
    String TENANT_CONSOLE = "tenant-console";
    String WORKFLOW_CONSOLE = "workflow-console";
    String GOOGLE_DOCS_CONSOLE = "google-docs-console";
    String MODEL_MSG_CONSOLE = "model-messages-console";

    /*
     * Used for installer
     */
    String INSTALLER_ALFRESCO = "installer-alfresco";
    String INSTALLER_ALFRESCO_SHARE = "installer-alfresco-share";
    String INSTALLER_ALFRESCO_PLATFORM = "installer-alfresco-platform";
    String UBUNTU = "ubuntu";

}
