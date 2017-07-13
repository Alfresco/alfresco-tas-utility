package org.alfresco.utility.application.gui.installer;

import static org.alfresco.utility.Utility.getTextFromClipboard;
import static org.sikuli.script.Mouse.WHEEL_DOWN;

import org.alfresco.utility.Utility;
import org.alfresco.utility.application.Focusable;
import org.alfresco.utility.application.gui.installer.ACSInstallerProperties.DESCRIPTION;
import org.alfresco.utility.application.gui.installer.ACSInstallerProperties.LANGUAGES;
import org.alfresco.utility.exception.CouldNotFindImageOnScreen;
import org.apache.commons.lang.SystemUtils;
import org.sikuli.api.robot.Key;
import org.sikuli.script.FindFailed;
import org.springframework.stereotype.Component;
import org.testng.Assert;

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

    public LanguageSelection onLanguageSelectionDialog() throws Exception
    {
        return new LanguageSelection();
    }

    public Dialog onDialog() throws Exception
    {
        return new Dialog();
    }

    public Warning onWarning() throws Exception
    {
        return new Warning();
    }

    public Setup onSetup() throws Exception
    {
        return new Setup();
    }
    
    public FrenchSetup onFrenchSetup() throws Exception
    {
        return new FrenchSetup();
    }

    public LicensePage onLicensePage() throws FindFailed, Exception
    {
        return new LicensePage();
    }

    public LicensePage navigateToLicenseForm() throws Exception
    {
        open();
        waitForInstallerToOpen();
        onLanguageSelectionDialog().clickOK();
        onSetup().clickNext();
        return new LicensePage();
    }

    public InstallationType onInstallationTypePage() throws Exception
    {
        return new InstallationType();
    }

    public InstallationType navigateToInstallationTypeForm() throws Exception
    {
        open();
        waitForInstallerToOpen();
        onLanguageSelectionDialog().clickOK();
        onSetup().clickNext();
        onLicensePage().acceptTheAgreement();
        onSetup().clickNext();
        return new InstallationType();
    }

    public InstallationFolder onInstallationFolderPage() throws Exception
    {
        return new InstallationFolder();
    }

    public InstallationFolder navigateToInstallationFolderForm() throws Exception
    {
        open();
        waitForInstallerToOpen();
        onLanguageSelectionDialog().clickOK();
        onSetup().clickNext();
        onLicensePage().acceptTheAgreement();
        onSetup().clickNext();
        onInstallationTypePage().chooseEasyInstall();
        onSetup().clickNext();
        return new InstallationFolder();
    }

    public DatabaseParameters onDatabaseParametersPage() throws Exception
    {
        return new DatabaseParameters();
    }

    public DatabaseConfiguration navigateToDatabaseServerParametersPage() throws Exception
    {
        open();
        waitForInstallerToOpen();
        onLanguageSelectionDialog().clickOK();
        onSetup().clickNext();
        onLicensePage().acceptTheAgreement();
        onSetup().clickNext();
        onInstallationTypePage().chooseAdvancedInstall();
        onSetup().clickNext()
                .clickNext()
                .clickNext();
        return new DatabaseConfiguration();
    }

    public DatabaseConfiguration onDatabaseConfigurationPage() throws Exception
    {
        return new DatabaseConfiguration();
    }

    public TomcatConfigurationPage onTomcatPortConfigurationPage() throws Exception
    {
        return new TomcatConfigurationPage();
    }

    public TomcatConfigurationPage navigateToTomcatPortConfigurationPage() throws Exception
    {
        open();
        waitForInstallerToOpen();
        onLanguageSelectionDialog().clickOK();
        onSetup().clickNext();
        onLicensePage().acceptTheAgreement();
        onSetup().clickNext();
        onInstallationTypePage().chooseAdvancedInstall();
        onSetup().clickNext()
                 .clickNext()
                 .clickNext()
                 .clickNext();
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

    public FtpPortPage navigateToFtpPortPage() throws Exception
    {
        open();
        waitForInstallerToOpen();
        onLanguageSelectionDialog().clickOK();
        onSetup().clickNext();
        onLicensePage().acceptTheAgreement();
        onSetup().clickNext();
        onInstallationTypePage().chooseAdvancedInstall();
        onSetup().clickNext()
                 .clickNext()
                 .clickNext()
                 .clickNext()
                 .clickNext()
                 .clickNext()
                 .clickNext();
        return new FtpPortPage();
    }

    public RmiPortPage onRmiPortPage() throws Exception
    {
        return new RmiPortPage();
    }

    public RmiPortPage navigateToRmiPortPage() throws Exception
    {
        open();
        waitForInstallerToOpen();
        onLanguageSelectionDialog().clickOK();
        onSetup().clickNext();
        onLicensePage().acceptTheAgreement();
        onSetup().clickNext();
        onInstallationTypePage().chooseAdvancedInstall();
        onSetup().clickNext()
                 .clickNext()
                 .clickNext()
                 .clickNext()
                 .clickNext()
                 .clickNext()
                 .clickNext()
                 .clickNext();
        return new RmiPortPage();
    }

    public LibreOfficeServerPortPage onLibreOfficeServerPortPage() throws Exception
    {
        return new LibreOfficeServerPortPage();
    }

    public LibreOfficeServerPortPage navigateToLibreOfficeServerPortPage() throws Exception
    {
        open();
        waitForInstallerToOpen();
        onLanguageSelectionDialog().clickOK();
        onSetup().clickNext();
        onLicensePage().acceptTheAgreement();
        onSetup().clickNext();
        onInstallationTypePage().chooseAdvancedInstall();
        onSetup().clickNext()
                 .clickNext()
                 .clickNext()
                 .clickNext()
                 .clickNext();
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

    public SelectComponents navigateToSelectComponentsForm() throws Exception
    {
        open();
        waitForInstallerToOpen();
        onLanguageSelectionDialog().clickOK();
        onSetup().clickNext();
        onLicensePage().acceptTheAgreement();
        onSetup().clickNext();
        onInstallationTypePage().chooseAdvancedInstall();
        onSetup().clickNext();
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
        
        public FrenchSetup startWithFrenchSetup() throws Exception
        {
            clickOn("languageSelection/title");
            clickOn("languageSelection/ok");
            return new FrenchSetup();
        }

        public void clickCancel() throws Exception
        {
            focus();
            clickOn("cancel");
            Utility.waitToLoopTime(WAIT_TIMEOUT);
        }

        @Override
        public LanguageSelection focus() throws Exception
        {
            clickOn("languageSelection/title");
            return this;
        }
        
        public LanguageSelection setLanguage(LANGUAGES language) throws CouldNotFindImageOnScreen {
        	clickOn("languageSelection/openLanguages");
        	clickOn(language.toString());
        	return this;
        }
        
        public LanguageSelection assertSelectedLanguageIs(LANGUAGES language) throws Exception
        {
            waitOn(language.toString());
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

        public Setup clickBack() throws Exception
        {
            focus();
            clickOn("back");
            return this;
        }

        public Setup checkSetupDescription() throws Exception
        {
            waitOn("setup/description");
            return this;
        }
        
        public Setup assertBackButtonIsDisabled() throws Exception
        {
            waitOn("back");
            return this;
        }
    }
    
    public class FrenchSetup implements Focusable<FrenchSetup>
    {
        public FrenchSetup() throws FindFailed, Exception
        {
            waitOn("languageSelection/languages/french/setup");
        }
        
        public FrenchSetup assertDescriptionIs(DESCRIPTION description) throws Exception {
			waitOn(description.toString());
        	return this;	
        }

		@Override
		public FrenchSetup focus() throws Exception {
			clickOn("languageSelection/languages/french/setup");
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

        public LicensePage acceptTheAgreement() throws Exception
        {
            focus();
            clickOn("license/iacceptagreement");
            return this;
        }

        public LicensePage doNotAcceptTheAgreement() throws Exception
        {
            focus();
            clickOn("license/notacceptagreement");
            return this;
        }

        public LicensePage scrollAgreement() throws Exception
        {
            focus();
            wheel(WHEEL_DOWN, 30);
            waitOn("license/scrolledBar");
            return this;
        }

        public LicensePage noOptionIsSelected() throws Exception
        {
            waitOn("license/iacceptagreement");
            waitOn("license/notacceptagreement");
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
            if (SystemUtils.IS_OS_MAC)
            {
                type(Key.ENTER);
            }
            else
                clickOn("dialog/yes");
            Utility.waitToLoopTime(WAIT_TIMEOUT);
        }

        public void clickNo() throws Exception
        {
            focus();
            if (SystemUtils.IS_OS_MAC)
            {
                type(Key.ESC);
            }
            else
                clickOn("dialog/no");
        }

        @Override
        public Dialog focus() throws CouldNotFindImageOnScreen
        {
            clickOn("dialog/doYouWantToAbort");
            clickOn("dialog/title");
            return this;
        }
    }

    /*
     * Generic warning
     */
    public class Warning implements Focusable<Warning>
    {
        public Warning() throws Exception
        {
            waitOn("warning/title");
            focus();
        }

        public void clickOK() throws Exception
        {
            focus();
            clickOn("warning/ok");
        }

        @Override
        public Warning focus() throws CouldNotFindImageOnScreen
        {
            clickOn("warning/title");
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

        public InstallationType chooseEasyInstall() throws Exception
        {
            focus();
            clickOn("installationType/easyInstall");
            return this;
        }

        public InstallationType chooseAdvancedInstall() throws Exception
        {
            focus();
            clickOn("installationType/advancedInstall");
            return this;
        }
    }

    /**
     * If easy installation type is selected, this page will be available
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

        public InstallationFolder setDestination(String destination) throws Exception
        {
            focus();            
            clearAndType(destination);
            return this;
        }

        public boolean isSelectedFolderNotEmptyWarningDisplayed() throws CouldNotFindImageOnScreen
        {
            return isPopUpDisplayed("installationFolder/selectedFolderNotEmpty");
        }

        public InstallationFolder assertInstallationFolderIs(String expectedFolder) throws Exception
        {
            copyToClipboard();
            Assert.assertEquals(getTextFromClipboard(), expectedFolder, "Installation folder is set.");
            return this;
        }
    }

    /**
     * If easy installation type is selected, this page will be available
     */
    public class AdminPassword implements Focusable<AdminPassword>
    {
        public AdminPassword() throws Exception
        {
            waitOn("adminPassword/title");
        }

        @Override
        public AdminPassword focus() throws Exception
        {
            clickOn("adminPassword/title");
            return this;
        }

        public AdminPassword setAdminPassword(String password) throws Exception
        {
            focus();
            clickOn("adminPassword/adminPassword");
            clearAndType(installerProperties.getAdminPassword());
            return this;
        }

        public AdminPassword setRepeatAdminPassword(String password) throws Exception
        {
            focus();
            clickOn("adminPassword/repeatPassword");
            clearAndType(installerProperties.getAdminPassword());
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

        public SelectComponents checkLibreOffice() throws CouldNotFindImageOnScreen
        {
            checkOn("selectComponents/libreOffice");
            return this;
        }

        public SelectComponents checkJava() throws CouldNotFindImageOnScreen
        {
            checkOn("selectComponents/java");
            return this;
        }

        public SelectComponents checkPostgreSQL() throws CouldNotFindImageOnScreen
        {
            checkOn("selectComponents/postgreSQL");
            return this;
        }

        public SelectComponents checkSolr1() throws CouldNotFindImageOnScreen
        {
            checkOn("selectComponents/solr1");
            return this;
        }

        public SelectComponents checkSolr4() throws CouldNotFindImageOnScreen
        {
            checkOn("selectComponents/solr4");
            return this;
        }

        public SelectComponents checkAlfrescoOfficeServices() throws CouldNotFindImageOnScreen
        {
            checkOn("selectComponents/alfrescoOfficeServices");
            return this;
        }

        public SelectComponents checkWebQuickStart() throws CouldNotFindImageOnScreen
        {
            checkOn("selectComponents/webQuickStart");
            return this;
        }

        public SelectComponents checkGoogleDocsIntegration() throws CouldNotFindImageOnScreen
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

        public DatabaseParameters setPort() throws Exception
        {
            clickOn("databaseParameters/port");
            clearAndType(installerProperties.getInstallerDBPort());
            return this;
        }

        public DatabaseParameters setPort(String port) throws Exception
        {
            focus();
            clearAndType(port);
            return this;
        }

        public boolean isDatabaseServerPortWarningMessageDisplayed() throws CouldNotFindImageOnScreen
        {
            return isPopUpDisplayed("databaseParameters/databaseServerPortWarningMessage");
        }

        public DatabaseParameters assertDatabasePortIs(String expectedPort) throws Exception
        {
            copyToClipboard();
            Assert.assertEquals(getTextFromClipboard(), expectedPort, "Database Server Port is set.");
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

        public DatabaseConfiguration setUsername() throws Exception
        {
            clickOn("databaseConfiguration/username");
            clearAndType(installerProperties.getInstallerDBUsername());
            return this;
        }

        public DatabaseConfiguration setPassword() throws Exception
        {
            clickOn("databaseConfiguration/password");
            clearAndType(installerProperties.getInstallerDBPassword());
            return this;
        }

        public DatabaseConfiguration verifyPassword() throws Exception
        {
            clickOn("databaseConfiguration/verify");
            clearAndType(installerProperties.getInstallerDBPassword());
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

        public TomcatConfigurationPage setWebServerDomain() throws Exception
        {
            clearAndType(installerProperties.getInstallerWebServerDomain());
            return this;
        }

        public TomcatConfigurationPage setTomcatServerPort() throws Exception
        {
            clickOn("tomcat/tomcatServerPort");
            clearAndType(installerProperties.getInstallerTomcatServerPort());
            return this;
        }

        public TomcatConfigurationPage setTomcatServerPort(String portNumber) throws Exception
        {
            clickOn("tomcat/tomcatServerPort");
            clearAndType(portNumber);
            return this;
        }

        public TomcatConfigurationPage setTomcatShutdownPort() throws Exception
        {
            clickOn("tomcat/tomcatShutdownPort");
            clearAndType(installerProperties.getInstallerTomcatShutdownPort());
            return this;
        }

        public TomcatConfigurationPage setTomcatShutdownPort(String portNumber) throws Exception
        {
            clickOn("tomcat/tomcatShutdownPort");
            clearAndType(portNumber);
            return this;
        }

        public TomcatConfigurationPage setTomcatSSLPort() throws Exception
        {
            clickOn("tomcat/tomcatSSLPort");
            clearAndType(installerProperties.getInstallerTomcatSSLPort());
            return this;
        }

        public TomcatConfigurationPage setTomcatSSLPort(String portNumber) throws Exception
        {
            clickOn("tomcat/tomcatSSLPort");
            clearAndType(portNumber);
            return this;
        }

        public TomcatConfigurationPage setTomcatAJPPort() throws Exception
        {
            clickOn("tomcat/tomcatAJPPort");
            clearAndType(installerProperties.getInstallerTomcatAJPPort());
            return this;
        }

        public TomcatConfigurationPage setTomcatAJPPort(String portNumber) throws Exception
        {
            clickOn("tomcat/tomcatAJPPort");
            clearAndType(portNumber);
            return this;
        }

        public boolean isTomcatServerPortWarningMessageDisplayed() throws CouldNotFindImageOnScreen
        {
            return isPopUpDisplayed("tomcat/tomcatServerPortWarning");
        }

        public boolean isTomcatShutdownPortWarningMessageDisplayed() throws CouldNotFindImageOnScreen
        {
            return isPopUpDisplayed("tomcat/tomcatShutdownPortWarning");
        }

        public boolean isTomcatSSLPortWarningMessageDisplayed() throws CouldNotFindImageOnScreen
        {
            return isPopUpDisplayed("tomcat/tomcatSSLPortWarning");
        }

        public boolean isTomcatAJPPortWarningMessageDisplayed() throws CouldNotFindImageOnScreen
        {
            return isPopUpDisplayed("tomcat/tomcatAJPPortWarning");
        }

        public boolean isTomcatPortCharacterWarningMessageDisplayed() throws CouldNotFindImageOnScreen
        {
            return isPopUpDisplayed("tomcat/charactersNotPermittedError");
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

        public ShardedSolrPage setYes() throws CouldNotFindImageOnScreen
        {
            clickOn("shardedSolr/yes");
            return this;
        }

        public ShardedSolrPage setNo() throws CouldNotFindImageOnScreen
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

        public FtpPortPage setFtpPort() throws Exception
        {
            clickOn("ftp/ftpPort");
            clearAndType(installerProperties.getInstallerFTPPort());
            return this;
        }

        public FtpPortPage setFtpPort(String portNumber) throws Exception
        {
            clickOn("ftp/ftpPort");
            clearAndType(portNumber);
            return this;
        }

        public boolean isFtpPortWarningMessageDisplayed() throws CouldNotFindImageOnScreen
        {
            return isPopUpDisplayed("ftp/ftpPortNotPermittedError");
        }

        public boolean isFtpCharacterWarningMessageDisplayed() throws CouldNotFindImageOnScreen
        {
            return isPopUpDisplayed("ftp/charactersNotPermittedError");
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

        public RmiPortPage setRmiPort() throws Exception
        {
            clickOn("rmi/rmiPort");
            clearAndType(installerProperties.getInstallerRMIPort());
            return this;
        }

        public RmiPortPage setRmiPort(String portNumber) throws Exception
        {
            clickOn("rmi/rmiPort");
            clearAndType(portNumber);
            return this;
        }

        public boolean isRmiPortWarningMessageDisplayed() throws CouldNotFindImageOnScreen
        {
            return isPopUpDisplayed("rmi/rmiPortNotPermittedError");
        }

        public boolean isRmiCharacterWarningMessageDisplayed() throws CouldNotFindImageOnScreen
        {
            return isPopUpDisplayed("rmi/charactersNotPermittedError");
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

        public ServiceStartupConfigurationPage setManual() throws CouldNotFindImageOnScreen
        {
            clickOn("serviceStartup/manual");
            return this;
        }

        public ServiceStartupConfigurationPage setAuto() throws CouldNotFindImageOnScreen
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

        public LibreOfficeServerPortPage setLibreOfficeServerPort() throws Exception
        {
            clickOn("libreOffice/libreOfficeServerPort");
            clearAndType(installerProperties.getInstallerLibreOfficeServerPort());
            return this;
        }

        public LibreOfficeServerPortPage setLibreOfficeServerPort(String portNumber) throws Exception
        {
            clickOn("libreOffice/libreOfficeServerPort");
            clearAndType(portNumber);
            return this;
        }

        public boolean isLibreOfficePortWarningMessageDisplayed() throws CouldNotFindImageOnScreen
        {
            return isPopUpDisplayed("libreOffice/libreOfficePortNotPermittedError");
        }

        public boolean isLibreOfficeCharacterWarningMessageDisplayed() throws CouldNotFindImageOnScreen
        {
            return isPopUpDisplayed("libreOffice/charactersNotPermittedError");
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

        public RemoteSolrConfigurationPage setRemoteSolrHost() throws Exception
        {
            clickOn("remoteSolr/remoteSolrHost");
            clearAndType(installerProperties.getInstallerRemoteSolrHost());
            return this;
        }

        public RemoteSolrConfigurationPage setRemoteSolrSSLPort() throws Exception
        {
            clickOn("remoteSolr/remoteSolrSSLPort");
            clearAndType(installerProperties.getInstallerRemoteSolrSSLPort());
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

    public LanguageSelection navigateToLanguageForm() throws Exception
    {
        open();
        waitForInstallerToOpen();
        return new LanguageSelection();
    }
    
    public Setup navigateToSetupForm() throws Exception
    {
        open();
        waitForInstallerToOpen();
        onLanguageSelectionDialog().clickOK();
        return new Setup();
    }

}