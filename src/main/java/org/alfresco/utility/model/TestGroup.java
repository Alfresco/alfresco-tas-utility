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
    public static String ENTERPRISE = "enterprise";
    public static String PROTOCOLS = "protocols";
    public static String SSO = "sso";
    public static String VALIDATION_CYCLE = "validation-cycle";

    /*
     * Used in Rest api mostly
     */
    public static String REST_API = "rest-api";
    public static String ACTIONS = "actions";
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
    public static String TRASHCAN = "trashcan";
    public static String TASKS = "tasks";
    public static String PROCESS_DEFINITION = "process-definitions";
    public static String ACTIVITIES = "activities";
    public static String EXTENTION_POINTS = "extension-points";
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
    public static String GROUPS = "groups";
    public static String DISCOVERY = "discovery";
    public static String DOWNLOADS = "downloads";
    public static String ALL_AMPS = "all_amps"; // this is a test group used to mark tests that are ONLY for all amps install
    public static String CLUSTER = "cluster";

    public static String WONT_FIX = "wont-fix";
    public static String OS_WIN = "windows"; // this will mark tests that needs to be executed ONLY on Windows
    public static String OS_LINUX = "linux"; // this will mark tests that needs to be executed ONLY on Unix
    public static String OS_MAC = "mac"; // this will mark tests that needs to be executed ONLY on Unix
    
    // Search: Minimum Version Required
    public static String ASS_1 = "ASS_1.0.0"; // Alfresco Search Services 1.0. Does not work with Solr4
    public static String ASS_112 = "ASS_1.1.2"; // Alfresco Search Services 1.1.2
    public static String ASS_12 = "ASS_1.2.0"; // Alfresco Search Services 1.2
    public static String PreASS_121 = "PreASS_1.2.1"; // Alfresco Search Services Prior to ASS 1.2.1
    public static String ASS_121 = "ASS_1.2.1"; // Alfresco Search Services 1.2.1
    public static String ASS_13 = "ASS_1.3.0"; // Alfresco Search Services 1.3
    public static String ASS_1302 = "ASS_1.3.0.2"; // Alfresco Search Services 1.3.0.2 (Fingerprint MNT)
    public static String ASS_14 = "ASS_1.4.0"; // Alfresco Search Services 1.4
    public static String ASS_MASTER_SLAVE = "ASS_Master_Slave"; // Alfresco Search Services using master slave configurations
    public static String ASS_MASTER ="ASS_Master"; // Alfresco search services using master/stand alone mode 
    
    public static String INSIGHT_10 = "InsightEngine_1.0.0"; // Alfresco Insight Engine 1.0
    public static String INSIGHT_11 = "InsightEngine_1.1.0"; // Alfresco Insight Engine 1.1
    public static String INSIGHT_12 = "InsightEngine_1.2.0"; // Alfresco Insight Engine 1.2
    public static String NOT_INSIGHT_ENGINE = "Not_InsightEngine"; // When Alfresco Insight Engine 1.0 isn't running
    public static String SOLR = "SOLR"; //To be used for tests for /solr/alfresco/* end-points
    
    public static String ACS_52n = "ACS_52n"; // Alfresco Content Services 5.2.n
    public static String ACS_60n = "ACS_60n"; // Alfresco Content Services 6.0 or above
    public static String ACS_61n = "ACS_61n"; // Alfresco Content Services 6.1 or above
    
    public static String SYNC_SERVICE_220 = "SS_2.2.0"; // Sync Service 2.2
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

    /*
     * Mark tests that require a certain module
     */
    public static String REQUIRE_SHARE = "require-share";
    /**
     * The tests are now using {@link #RENDITIONS}
     */
    @Deprecated
    public static String REQUIRE_TRANSFORMATION = "require-transformation";
    // mark transformation/rendition tests that are not (yet) supported by Transform Service
    public static String NOT_SUPPORTED_BY_ATS = "not-supported-by-ats";
    public static String REQUIRE_JMX = "require-jmx";
    public static String REQUIRE_SOLR = "require-solr";

    //mark the test that it requires one amp installed
    public static String REQUIRES_AMP = "requires=amp";

    //Marks the test as renditions regression test, these tests run in their own separate test suit
    /**
     * The tests are now using {@link #RENDITIONS}
     */
    @Deprecated
    public static String RENDITIONS_REGRESSION = "renditions-regression";
}
