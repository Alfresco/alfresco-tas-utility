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
    public static String REGRESSION = "regression";
    public static String CORE = "core";
    public static String FULL = "full";
    public static String PROTOCOLS = "protocols";
    public static String SSO = "sso";
    public static String VALIDATION_CYCLE = "validation-cycle";

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
    public static String SHAREDLINKS = "shared-links";
    public static String SYNC_API = "sync-api";
    public static String TAGS = "tags";
    public static String TASKS = "tasks";
    public static String PROCESS_DEFINITION = "process-definitions";
    public static String ACTIVITIES = "activities";
    public static String EXTENTION_POINTS = "extention-points";
    public static String NODES = "nodes";
    public static String AUTH = "auth";
    public static String SEARCH = "search";
    public static String RENDITIONS = "renditions";
    public static String AUDIT = "audit";
    public static String IMAP = "imap";
    public static String SMTP = "smtp";
    public static String CIFS = "cifs";
    public static String CMIS = "cmis";
    public static String QUERIES = "queries";
    public static String WEBDAV = "webdav";
    public static String FTP = "ftp";
    public static String AOS = "aos";
    public static String INTEGRATION = "integration";
    public static String CONTENT = "content";
    public static String PREUPGRADE = "pre-upgrade";
    public static String POSTUPGRADE = "post-upgrade";
    public static String SHARE = "share";
    public static String BROWSER = "browser";
    public static String SECURITY = "security";

    public static String WONT_FIX = "wont-fix";
    public static String OS_WIN = "windows"; // this will mark tests that needs to be executed ONLY on Windows
    public static String OS_LINUX = "linux"; // this will mark tests that needs to be executed ONLY on Unix
    public static String OS_MAC = "mac"; // this will mark tests that needs to be executed ONLY on Unix
    public static String ASS_1 = "ASS_1.0.0"; // Alfresco Search Services 1.0

    /*
     * Used in Share-PO
     */
    public static String SITES_FEATURES = "sites-features";
    public static String ADMIN_TOOLS = "admin-tools";
    public static String USER_DASHBOARD = "user-dashboard";
    public static String GOOGLE_DOCS = "google-docs";
    public static String USER = "user";
    public static String MANAGE_INVITES = "manage-invites";

    /*
     * Used in Admin Console
     */
    public static String TENANT_CONSOLE = "tenant-console";
    public static String WORKFLOW_CONSOLE = "workflow-console";
    public static String GOOGLE_DOCS_CONSOLE = "google-docs-console";
    public static String MODEL_MSG_CONSOLE = "model-messages-console";

    /*
     * Used in GUI Installer
     */     
    public static String INSTALLER_ALFRESCO = "installer-alfresco";
    public static String INSTALLER_ALFRESCO_SHARE = "installer-alfresco-share";
    public static String INSTALLER_ALFRESCO_PLATFORM = "installer-alfresco-platform";
    
    /*
     * Used in Identity Provider
     */
    public static String AUTHENTICATION_INTERNAL = "authentication-internal";
    public static String AUTHENTICATION_KERBEROS = "authentication-kerberos";
    public static String AUTHENTICATION_OPENLDAP = "authentication-openldap";
    public static String AUTHENTICATION_ADLDAP = "authentication-adldap";
    public static String AUTHENTICATION_NTLM_SSO = "authentication-ntlm-sso";
    public static String AUTHENTICATION_ORACLE_ACTIVEDIR = "authentication-oracle-active-directory";
    public static String AUTHENTICATION_CHAIN = "authentication-chain";
    
    /*
     * Used in Cryptographic password hashing
     */
    public static String PASSWORD_ENCONDING = "password-encoding";
    public static String MD4 = "md4";
    public static String SHA256 = "sha256";
    public static String BCRYPT10 = "bcrypt10";
}
