package org.alfresco.utility.application.gui.installer;

import org.alfresco.utility.application.Focusable;
import org.alfresco.utility.exception.CouldNotFindApplicationActionImage;
import org.sikuli.api.robot.Key;
import org.sikuli.script.FindFailed;
import org.springframework.stereotype.Component;

/**
 * Sikuli approach of interacting with Alfresco Installer.
 * Following a Wizard base design pattern
 * 
 * @author Paul Brodner
 */
@Component
public class ACSInstaller extends ACSWizard implements Installable
{
    @SuppressWarnings("unchecked")
    @Override
    public LanguageSelection waitForInstallerToOpen() throws Exception
    {
        return new LanguageSelection();
    }

    public Dialog onDialog() throws Exception
    {
        return new Dialog();
    }

    public Setup onSetup() throws Exception
    {
        return new Setup();
    }

    public LicensePage onLicensePage() throws FindFailed, Exception
    {
        return new LicensePage();
    }

    public InstallationType onInstallationTypePage() throws Exception
    {
        return new InstallationType();
    }
    
    public InstallationFolder onInstallationFolderPage() throws Exception
    {
        return new InstallationFolder();
    }
    
    public DatabaseParameters onDatabaseParametersPage() throws Exception
    {
        return new DatabaseParameters();
    }

    public DatabaseConfiguration onDatabaseConfigurationPage() throws Exception
    {
        return new DatabaseConfiguration();
    }

    public TomcatConfigurationPage onTomcatPortConfigurationPage() throws Exception
    {
        return new TomcatConfigurationPage();
    }

    public ShardedSolrPage onShardedSolrInstallationPage() throws Exception
    {
        return new ShardedSolrPage();
    }

    public FtpPortPage onFtpPortPage() throws Exception
    {
        return new FtpPortPage();
    }

    public RmiPortPage onRmiPortPage() throws Exception
    {
        return new RmiPortPage();
    }

    public LibreOfficeServerPortPage onLibreOfficeServerPortPage() throws Exception
    {
        return new LibreOfficeServerPortPage();
    }

    public RemoteSolrConfigurationPage onRemoteSolrConfigurationPage() throws Exception
    {
        return new RemoteSolrConfigurationPage();
    }

    public ServiceStartupConfigurationPage onServiceStartupConfigurationPage() throws Exception
    {
        return new ServiceStartupConfigurationPage();
    }

    public SelectComponents onSelectComponentsPage() throws Exception
    {
        return new SelectComponents();
    }

    public AdminPassword onAdminPasswordPage() throws Exception
    {
        return new AdminPassword();
    }

    public ReadyToInstallPage onReadyToInstallPage() throws Exception
    {
        return new ReadyToInstallPage();
    }

    /**
     * Language Selection - 1st Dialog
     */
    public class LanguageSelection implements Focusable<LanguageSelection>
    {
        public LanguageSelection() throws Exception
        {
            waitOn("languageSelection/dialog");
        }

        public Setup clickOK() throws Exception
        {
            clickOn("languageSelection/title");
            clickOn("languageSelection/ok");
            return new Setup();
        }

        public void clickCancel() throws Exception
        {
            focus();
            clickOn("cancel");
        }

        @Override
        public LanguageSelection focus() throws Exception
        {
            clickOn("languageSelection/title");
            return this;
        }
    }

    /**
     * Setup - 2nd Dialog
     */
    public class Setup implements Focusable<Setup>
    {
        public Setup() throws FindFailed, Exception
        {
            waitOn("setup");
        }

        @Override
        public Setup focus() throws Exception
        {
            clickOn("setup");
            return this;
        }

        public Dialog clickCancel() throws Exception
        {
            focus();
            clickOn("cancel");
            return new Dialog();
        }

        public Setup clickNext() throws Exception
        {
            focus();
            clickOn("next");
            return this;
        }
    }

    /**
     * Licensing - 3rd Page
     */
    public class LicensePage implements Focusable<LicensePage>
    {
        public LicensePage() throws Exception
        {
            waitOn("license/licenseAgreement");
        }

        @Override
        public LicensePage focus() throws Exception
        {
            clickOn("license/licenseAgreement");
            return this;
        }

        public LicensePage acceptTheAgreement() throws CouldNotFindApplicationActionImage
        {
            clickOn("license/iacceptagreement");
            return this;
        }

        public LicensePage doNotAcceptTheAgreement() throws Exception
        {
            clickOn("license/notacceptagreement");
            return this;
        }
    }

    /*
     * Generic dialog
     */
    public class Dialog implements Focusable<Dialog>
    {
        public Dialog() throws Exception
        {
            waitOn("dialog/doYouWantToAbort");
            focus();
        }

        public void clickYes() throws Exception
        {
            focus();
//            type(Key.ENTER);
            clickOn("dialog/yes");
        }

        public void clickNo() throws Exception
        {
            focus();
//            type(Key.ESC);
            clickOn("dialog/no");
        }

        @Override
        public Dialog focus() throws CouldNotFindApplicationActionImage
        {
            clickOn("dialog/doYouWantToAbort");
            return this;
        }
    }

    /**
     * Installation Type -4th Page
     */
    public class InstallationType implements Focusable<InstallationType>
    {
        public InstallationType() throws Exception
        {
            waitOn("installationType/title");
        }

        @Override
        public InstallationType focus() throws Exception
        {
            clickOn("installationType/title");
            return this;
        }

        public InstallationType chooseEasyInstall() throws CouldNotFindApplicationActionImage
        {
            clickOn("installationType/easyInstall");
            return this;
        }

        public InstallationType chooseAdvancedInstall() throws CouldNotFindApplicationActionImage
        {
            clickOn("installationType/advancedInstall");
            return this;
        }
    }
    
    /**
     * If easy installation type is selected, this page will be available
     *
     */
    public class InstallationFolder implements Focusable<InstallationFolder>
    {
        public InstallationFolder() throws Exception
        {
            waitOn("installationFolder/title");
        }

        @Override
        public InstallationFolder focus() throws Exception
        {
            clickOn("installationFolder/title");
            return this;
        }

        public InstallationFolder setDestination() throws CouldNotFindApplicationActionImage
        {            
            //clickOn("installationFolder/folderLocation");
            type(Key.DELETE);
            type(installerProperties.getInstallerDestinationPath().getPath());
            return this;
        }
    }

    /**
     * If easy installation type is selected, this page will be available
     *
     */
    public class AdminPassword implements Focusable<AdminPassword>
    {
        public AdminPassword() throws Exception
        {
            waitOn("adminPassword/adminPassword");
        }

        @Override
        public AdminPassword focus() throws Exception
        {
            clickOn("adminPassword/adminPassword");
            return this;
        }

        public AdminPassword setAdminPassword() throws CouldNotFindApplicationActionImage
        {
            clickOn("adminPassword/password");
            type(Key.DELETE);
            type(installerProperties.getAdminPassword());
            return this;
        }

        public AdminPassword setRepeatPassword() throws CouldNotFindApplicationActionImage
        {
            clickOn("adminPassword/repeatPassword");
            type(Key.DELETE);
            type(installerProperties.getAdminPassword());
            return this;
        }
    }

    /**
     * If advanced installation type is selected, this page will be visible
     */
    public class SelectComponents implements Focusable<SelectComponents>
    {
        public SelectComponents() throws Exception
        {
            waitOn("selectComponents/title");
        }

        @Override
        public SelectComponents focus() throws Exception
        {
            clickOn("selectComponents/title");
            return this;
        }

        public SelectComponents checkLibreOffice() throws CouldNotFindApplicationActionImage
        {
            checkOn("selectComponents/libreOffice");
            return this;
        }

        public SelectComponents checkJava() throws CouldNotFindApplicationActionImage
        {
            checkOn("selectComponents/java");
            return this;
        }

        public SelectComponents checkPostgreSQL() throws CouldNotFindApplicationActionImage
        {
            checkOn("selectComponents/postgreSQL");
            return this;
        }

        public SelectComponents checkSolr1() throws CouldNotFindApplicationActionImage
        {
            checkOn("selectComponents/solr1");
            return this;
        }

        public SelectComponents checkSolr4() throws CouldNotFindApplicationActionImage
        {
            checkOn("selectComponents/solr4");
            return this;
        }

        public SelectComponents checkAlfrescoOfficeServices() throws CouldNotFindApplicationActionImage
        {
            checkOn("selectComponents/alfrescoOfficeServices");
            return this;
        }

        public SelectComponents checkWebQuickStart() throws CouldNotFindApplicationActionImage
        {
            checkOn("selectComponents/webQuickStart");
            return this;
        }

        public SelectComponents checkGoogleDocsIntegration() throws CouldNotFindApplicationActionImage
        {
            checkOn("selectComponents/googleDocsIntegration");
            return this;
        }
    }
    
    public class DatabaseParameters implements Focusable<DatabaseParameters>
    {
        public DatabaseParameters() throws Exception
        {
            waitOn("databaseParameters/title");
        }

        @Override
        public DatabaseParameters focus() throws Exception
        {
            clickOn("databaseParameters/title");
            return this;
        }

        public DatabaseParameters setPort() throws CouldNotFindApplicationActionImage
        {            
            type(Key.DELETE);
            type(installerProperties.getProperty("db.port"));
            return this;
        }
    }

    public class DatabaseConfiguration implements Focusable<DatabaseConfiguration>
    {
        public DatabaseConfiguration() throws Exception
        {
            waitOn("databaseConfiguration/title");
        }

        @Override
        public DatabaseConfiguration focus() throws Exception
        {
            clickOn("databaseConfiguration/title");
            return this;
        }

        public DatabaseConfiguration setUsername() throws CouldNotFindApplicationActionImage
        {
            clickOn("databaseConfiguration/username");
            type(installerProperties.getProperty("db.username"));
            return this;
        }

        public DatabaseConfiguration setPassword() throws CouldNotFindApplicationActionImage
        {
            clickOn("databaseConfiguration/password");
            type(installerProperties.getProperty("db.password"));
            return this;
        }

        public DatabaseConfiguration verifyPassword() throws CouldNotFindApplicationActionImage
        {
            clickOn("databaseConfiguration/verify");
            type(installerProperties.getProperty("db.password"));
            return this;
        }
    }

    public class TomcatConfigurationPage implements Focusable<TomcatConfigurationPage>
    {
        public TomcatConfigurationPage() throws Exception
        {
            waitOn("tomcat/title");
        }

        @Override
        public TomcatConfigurationPage focus() throws Exception
        {
            clickOn("tomcat/title");
            return this;
        }

        public TomcatConfigurationPage setWebServerDomain() throws CouldNotFindApplicationActionImage
        {
            type("a", Key.CTRL);
            type(installerProperties.getProperty("web.server.domain"));
            return this;
        }

        public TomcatConfigurationPage setTomcatServerPort() throws CouldNotFindApplicationActionImage
        {
            clickOn("tomcat/tomcatServerPort");
            type("a", Key.CTRL);
            type(installerProperties.getProperty("tomcat.server.port"));
            return this;
        }

        public TomcatConfigurationPage setTomcatShutdownPort() throws CouldNotFindApplicationActionImage
        {
            clickOn("tomcat/tomcatShutdownPort");
            type("a", Key.CTRL);
            type(installerProperties.getProperty("tomcat.shutdown.port"));
            return this;
        }

        public TomcatConfigurationPage setTomcatSSLPort() throws CouldNotFindApplicationActionImage
        {
            clickOn("tomcat/tomcatSSLPort");
            type("a", Key.CTRL);
            type(installerProperties.getProperty("tomcat.ssl.port"));
            return this;
        }

        public TomcatConfigurationPage setTomcatAJPPort() throws CouldNotFindApplicationActionImage
        {
            clickOn("tomcat/tomcatAJPPort");
            type("a", Key.CTRL);
            type(installerProperties.getProperty("tomcat.ajp.port"));
            return this;
        }
    }

    public class ShardedSolrPage implements Focusable<ShardedSolrPage>
    {
        public ShardedSolrPage() throws Exception
        {
            waitOn("shardedSolr/title");
        }

        @Override
        public ShardedSolrPage focus() throws Exception
        {
            clickOn("shardedSolr/title");
            return this;
        }

        public ShardedSolrPage setYes() throws CouldNotFindApplicationActionImage
        {
            clickOn("shardedSolr/yes");
            return this;
        }

        public ShardedSolrPage setNo() throws CouldNotFindApplicationActionImage
        {
            clickOn("shardedSolr/no");
            return this;
        }
    }

    public class FtpPortPage implements Focusable<FtpPortPage>
    {
        public FtpPortPage() throws Exception
        {
            waitOn("ftp/title");
        }

        @Override
        public FtpPortPage focus() throws Exception
        {
            clickOn("ftp/title");
            return this;
        }

        public FtpPortPage setFtpPort() throws CouldNotFindApplicationActionImage
        {
            clickOn("ftp/ftpPort");
            type("a", Key.CTRL);
            type(installerProperties.getProperty("ftp.port"));
            return this;
        }
    }

    public class RmiPortPage implements Focusable<RmiPortPage>
    {
        public RmiPortPage() throws Exception
        {
            waitOn("rmi/title");
        }

        @Override
        public RmiPortPage focus() throws Exception
        {
            clickOn("rmi/title");
            return this;
        }

        public RmiPortPage setRmiPort() throws CouldNotFindApplicationActionImage
        {
            clickOn("rmi/rmiPort");
            type("a", Key.CTRL);
            type(installerProperties.getProperty("rmi.port"));
            return this;
        }
    }

    public class ServiceStartupConfigurationPage implements Focusable<ServiceStartupConfigurationPage>
    {
        public ServiceStartupConfigurationPage() throws Exception
        {
            waitOn("serviceStartup/title");
        }

        @Override
        public ServiceStartupConfigurationPage focus() throws Exception
        {
            clickOn("serviceStartup/title");
            return this;
        }

        public ServiceStartupConfigurationPage setManual() throws CouldNotFindApplicationActionImage
        {
            clickOn("serviceStartup/manual");
            return this;
        }

        public ServiceStartupConfigurationPage setAuto() throws CouldNotFindApplicationActionImage
        {
            clickOn("serviceStartup/auto");
            return this;
        }
    }

    public class LibreOfficeServerPortPage implements Focusable<LibreOfficeServerPortPage>
    {
        public LibreOfficeServerPortPage() throws Exception
        {
            waitOn("libreOffice/title");
        }

        @Override
        public LibreOfficeServerPortPage focus() throws Exception
        {
            clickOn("libreOffice/title");
            return this;
        }

        public LibreOfficeServerPortPage setRmiPort() throws CouldNotFindApplicationActionImage
        {
            clickOn("libreOffice/libreOfficeServerPort");
            type("a", Key.CTRL);
            type(installerProperties.getProperty("libre.office.server.port"));
            return this;
        }
    }

    public class RemoteSolrConfigurationPage implements Focusable<RemoteSolrConfigurationPage>
    {
        public RemoteSolrConfigurationPage() throws Exception
        {
            waitOn("remoteSolr/title");
        }

        @Override
        public RemoteSolrConfigurationPage focus() throws Exception
        {
            clickOn("remoteSolr/title");
            return this;
        }

        public RemoteSolrConfigurationPage setRemoteSolrHost() throws CouldNotFindApplicationActionImage
        {
            clickOn("remoteSolr/remoteSolrHost");
            type("a", Key.CTRL);
            type(installerProperties.getProperty("remote.solr.host"));
            return this;
        }

        public RemoteSolrConfigurationPage setRemoteSolrSSLPort() throws CouldNotFindApplicationActionImage
        {
            clickOn("remoteSolr/remoteSolrSSLPort");
            type("a", Key.CTRL);
            type(installerProperties.getProperty("remote.solr.ssl.port"));
            return this;
        }
    }

    public class ReadyToInstallPage implements Focusable<ReadyToInstallPage>
    {
        public ReadyToInstallPage() throws Exception
        {
            waitOn("ready/title");
        }

        @Override
        public ReadyToInstallPage focus() throws Exception
        {
            clickOn("ready/title");
            return this;
        }
    }

    @Override
    public ACSInstaller focus() throws Exception
    {
        onSetup().focus();
        return this;
    }

}