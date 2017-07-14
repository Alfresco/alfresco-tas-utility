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
@Component public class ACSInstaller extends ACSWizard implements Installable
{
    @SuppressWarnings("unchecked") @Override public LanguageSelection waitForInstallerToOpen() throws Exception
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

    public InstallingPage onInstallingPage() throws Exception
    {
        return new InstallingPage();
    }

    public CompletingSetup onCompletingSetupPage() throws Exception
    {
        return new CompletingSetup();
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

        @Override public LanguageSelection focus() throws Exception
        {
            clickOn("languageSelection/title");
            return this;
        }

        public LanguageSelection setLanguage(LANGUAGES language) throws CouldNotFindImageOnScreen
        {
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

        @Override public Setup focus() throws Exception
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

        public FrenchSetup assertDescriptionIs(DESCRIPTION description) throws Exception
        {
            waitOn(description.toString());
            return this;
        }

        @Override public FrenchSetup focus() throws Exception
        {
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

        @Override public LicensePage focus() throws Exception
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

        @Override public Dialog focus() throws CouldNotFindImageOnScreen
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

        @Override public Warning focus() throws CouldNotFindImageOnScreen
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

        @Override public InstallationType focus() throws Exception
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

        @Override public InstallationFolder focus() throws Exception
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

        @Override public AdminPassword focus() throws Exception
        {
            clickOn("adminPassword/title");
            return this;
        }

        public AdminPassword setAdminPassword(String password) throws Exception
        {
            focus();
            clickOn("adminPassword/adminPassword");
            clearAndType(password);
            return this;
        }

        public AdminPassword setRepeatAdminPassword(String password) throws Exception
        {
            focus();
            clickOn("adminPassword/repeatPassword");
            clearAndType(password);
            return this;
        }

        public void assertPasswordsDoNotMatchWarningIsDisplayed() throws CouldNotFindImageOnScreen
        {
            Assert.assertTrue(isPopUpDisplayed("adminPassword/passwordsDoNotMatchWarning"), "Passwords do not match warning message is not displayed.");
        }

        public void assertPasswordToShortWarningIsDisplayed() throws CouldNotFindImageOnScreen
        {
           Assert.assertTrue(isPopUpDisplayed("adminPassword/passwordsToShortWarning"), "Passwords should be longer than 3 characters warning is not displayed.");
        }

        public void assertReadyToInstallFormIsDisplayed() throws CouldNotFindImageOnScreen
        {
          Assert.assertTrue(isPopUpDisplayed("ready/readyToInstall"), "The next form, 'Ready to install' is not displayed");
        }

        public void assertPreviouslyProvidedPasswordIsAvailable() throws CouldNotFindImageOnScreen
        {
            Assert.assertTrue(isPopUpDisplayed("adminPassword/adminPassPreviousleyProvidedData"), "The previousley provided password is no longer available");
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

        @Override public SelectComponents focus() throws Exception
        {
            clickOn("selectComponents/title");
            return this;
        }

        public SelectComponents checkLibreOffice() throws CouldNotFindImageOnScreen
        {
            clickOn("selectComponents/libreOffice/label");
            checkOn("selectComponents/libreOffice/unchecked");
            return this;
        }

        public SelectComponents uncheckLibreOffice() throws CouldNotFindImageOnScreen
        {
            clickOn("selectComponents/libreOffice/label");
            checkOn("selectComponents/libreOffice/checked");
            return this;
        }

        public SelectComponents assertLibreOfficeIsChecked() throws Exception
        {
            clickOn("selectComponents/libreOffice/label");
            waitOn("selectComponents/libreOffice/checked");
            return this;
        }

        public SelectComponents assertLibreOfficeIsUnchecked() throws Exception
        {
            clickOn("selectComponents/libreOffice/label");
            waitOn("selectComponents/libreOffice/unchecked");
            return this;
        }

        public SelectComponents verifyLibreOfficeDescription() throws Exception
        {
            clickOn("selectComponents/libreOffice/label");
            waitOn("selectComponents/libreOffice/description");
            return this;
        }

        public SelectComponents checkJava()  throws CouldNotFindImageOnScreen
        {
            clickOn("selectComponents/java/label");
            checkOn("selectComponents/java/unchecked");
            return this;
        }

        public SelectComponents uncheckJava() throws CouldNotFindImageOnScreen
        {
            clickOn("selectComponents/java/label");
            checkOn("selectComponents/java/checked");
            return this;
        }

        public SelectComponents assertJavaIsChecked() throws Exception
        {
            clickOn("selectComponents/java/label");
            waitOn("selectComponents/java/checked");
            return this;
        }

        public SelectComponents assertJavaIsUnchecked() throws Exception
        {
            clickOn("selectComponents/java/label");
            waitOn("selectComponents/java/unchecked");
            return this;
        }

        public SelectComponents verifyJavaDescription() throws Exception
        {
            clickOn("selectComponents/java/label");
            waitOn("selectComponents/java/description");
            return this;
        }

        public SelectComponents checkPostgreSQL()  throws CouldNotFindImageOnScreen
        {
            clickOn("selectComponents/postgreSQL/label");
            checkOn("selectComponents/postgreSQL/unchecked");
            return this;
        }

        public SelectComponents uncheckPostgreSQL() throws CouldNotFindImageOnScreen
        {
            clickOn("selectComponents/postgreSQL/label");
            checkOn("selectComponents/postgreSQL/checked");
            return this;
        }

        public SelectComponents assertPostgreSQLIsChecked() throws Exception
        {
            clickOn("selectComponents/postgreSQL/label");
            waitOn("selectComponents/postgreSQL/checked");
            return this;
        }

        public SelectComponents assertPostgreSQLIsUnchecked() throws Exception
        {
            clickOn("selectComponents/postgreSQL/label");
            waitOn("selectComponents/postgreSQL/unchecked");
            return this;
        }

        public SelectComponents verifyPostgreSQLDescription() throws Exception
        {
            clickOn("selectComponents/postgreSQL/label");
            waitOn("selectComponents/postgreSQL/description");
            return this;
        }

        public SelectComponents checkSolr1()  throws CouldNotFindImageOnScreen
        {
            clickOn("selectComponents/solr1/label");
            checkOn("selectComponents/solr1/unchecked");
            return this;
        }

        public SelectComponents uncheckSolr1() throws CouldNotFindImageOnScreen
        {
            clickOn("selectComponents/solr1/label");
            checkOn("selectComponents/solr1/checked");
            return this;
        }

        public SelectComponents assertSolr1IsChecked() throws Exception
        {
            clickOn("selectComponents/solr1/label");
            waitOn("selectComponents/solr1/checked");
            return this;
        }

        public SelectComponents assertSolr1IsUnchecked() throws Exception
        {
            clickOn("selectComponents/solr1/label");
            waitOn("selectComponents/solr1/unchecked");
            return this;
        }

        public SelectComponents verifySolr1Description() throws Exception
        {
            clickOn("selectComponents/solr1/label");
            waitOn("selectComponents/solr1/description");
            return this;
        }

        public SelectComponents checkSolr4()  throws CouldNotFindImageOnScreen
        {
            clickOn("selectComponents/solr4/label");
            checkOn("selectComponents/solr4/unchecked");
            return this;
        }

        public SelectComponents uncheckSolr4() throws CouldNotFindImageOnScreen
        {
            clickOn("selectComponents/solr4/label");
            checkOn("selectComponents/solr4/checked");
            return this;
        }

        public SelectComponents assertSolr4IsChecked() throws Exception
        {
            clickOn("selectComponents/solr4/label");
            waitOn("selectComponents/solr4/checked");
            return this;
        }

        public SelectComponents assertSolr4IsUnchecked() throws Exception
        {
            clickOn("selectComponents/solr4/label");
            waitOn("selectComponents/solr4/unchecked");
            return this;
        }

        public SelectComponents verifySolr4Description() throws Exception
        {
            clickOn("selectComponents/solr4/label");
            waitOn("selectComponents/solr4/description");
            return this;
        }

        public SelectComponents checkAlfrescoOfficeServices()  throws CouldNotFindImageOnScreen
        {
            clickOn("selectComponents/alfrescoOfficeServices/label");
            checkOn("selectComponents/alfrescoOfficeServices/unchecked");
            return this;
        }

        public SelectComponents uncheckAlfrescoOfficeServices() throws CouldNotFindImageOnScreen
        {
            clickOn("selectComponents/alfrescoOfficeServices/label");
            checkOn("selectComponents/alfrescoOfficeServices/checked");
            return this;
        }

        public SelectComponents assertAlfrescoOfficeServicesIsChecked() throws Exception
        {
            clickOn("selectComponents/alfrescoOfficeServices/label");
            waitOn("selectComponents/alfrescoOfficeServices/checked");
            return this;
        }

        public SelectComponents assertAlfrescoOfficeServicesIsUnchecked() throws Exception
        {
            clickOn("selectComponents/alfrescoOfficeServices/label");
            waitOn("selectComponents/alfrescoOfficeServices/unchecked");
            return this;
        }

        public SelectComponents verifyAlfrescoOfficeServicesDescription() throws Exception
        {
            clickOn("selectComponents/alfrescoOfficeServices/label");
            waitOn("selectComponents/alfrescoOfficeServices/description");
            return this;
        }

        public SelectComponents checkWebQuickStart()  throws CouldNotFindImageOnScreen
        {
            clickOn("selectComponents/webQuickStart/label");
            checkOn("selectComponents/webQuickStart/unchecked");
            return this;
        }

        public SelectComponents uncheckWebQuickStart() throws CouldNotFindImageOnScreen
        {
            clickOn("selectComponents/webQuickStart/label");
            checkOn("selectComponents/webQuickStart/checked");
            return this;
        }

        public SelectComponents assertWebQuickStartIsChecked() throws Exception
        {
            clickOn("selectComponents/webQuickStart/label");
            waitOn("selectComponents/webQuickStart/checked");
            return this;
        }

        public SelectComponents assertWebQuickStartIsUnchecked() throws Exception
        {
            clickOn("selectComponents/webQuickStart/label");
            waitOn("selectComponents/webQuickStart/unchecked");
            return this;
        }

        public SelectComponents verifyWebQuickStartDescription() throws Exception
        {
            clickOn("selectComponents/webQuickStart/label");
            waitOn("selectComponents/webQuickStart/description");
            return this;
        }

        public SelectComponents checkGoogleDocsIntegration()  throws CouldNotFindImageOnScreen
        {
            clickOn("selectComponents/googleDocsIntegration/label");
            checkOn("selectComponents/googleDocsIntegration/unchecked");
            return this;
        }

        public SelectComponents uncheckGoogleDocsIntegration() throws CouldNotFindImageOnScreen
        {
            clickOn("selectComponents/googleDocsIntegration/label");
            checkOn("selectComponents/googleDocsIntegration/checked");
            return this;
        }

        public SelectComponents assertGoogleDocsIntegrationIsChecked() throws Exception
        {
            clickOn("selectComponents/googleDocsIntegration/label");
            waitOn("selectComponents/googleDocsIntegration/checked");
            return this;
        }

        public SelectComponents assertGoogleDocsIntegrationIsUnchecked() throws Exception
        {
            clickOn("selectComponents/googleDocsIntegration/label");
            waitOn("selectComponents/googleDocsIntegration/unchecked");
            return this;
        }

        public SelectComponents verifyGoogleDocsIntegrationDescription() throws Exception
        {
            clickOn("selectComponents/googleDocsIntegration/label");
            waitOn("selectComponents/googleDocsIntegration/description");
            return this;
        }

        public SelectComponents clickAlfrescoContentServices() throws CouldNotFindImageOnScreen
        {
            clickOn("selectComponents/alfrescoContentServices/labelCheckedAndDisabled");
            checkOn("selectComponents/alfrescoContentServices/checked");
            return this;
        }

        public SelectComponents assertAlfrescoContentServicesIsCheckedAndDisabled() throws Exception
        {
            waitOn("selectComponents/alfrescoContentServices/labelCheckedAndDisabled");
            return this;
        }

        public SelectComponents verifyAlfrescoContentServicesDescription() throws Exception
        {
            clickOn("selectComponents/alfrescoContentServices/labelCheckedAndDisabled");
            waitOn("selectComponents/alfrescoContentServices/description");
            return this;
        }
    }

    public class DatabaseParameters implements Focusable<DatabaseParameters>
    {
        public DatabaseParameters() throws Exception
        {
            waitOn("databaseParameters/title");
        }

        @Override public DatabaseParameters focus() throws Exception
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

        @Override public DatabaseConfiguration focus() throws Exception
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

        @Override public TomcatConfigurationPage focus() throws Exception
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

        @Override public ShardedSolrPage focus() throws Exception
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

        @Override public FtpPortPage focus() throws Exception
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

        @Override public RmiPortPage focus() throws Exception
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

        @Override public ServiceStartupConfigurationPage focus() throws Exception
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

        @Override public LibreOfficeServerPortPage focus() throws Exception
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

        @Override public RemoteSolrConfigurationPage focus() throws Exception
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

        @Override public ReadyToInstallPage focus() throws Exception
        {
            clickOn("ready/title");
            return this;
        }
    }

    public class InstallingPage implements Focusable<InstallingPage>
    {
        public InstallingPage() throws Exception
        {
            waitOn("installing/title");
        }

        @Override
        public InstallingPage focus() throws Exception
        {
            clickOn("installing/title");
            return this;
        }
    }

    public class CompletingSetup implements Focusable<CompletingSetup>
    {
        public CompletingSetup() throws Exception
        {
            waitOn("completingSetup/title", 600);
        }

        @Override
        public CompletingSetup focus() throws Exception
        {
            clickOn("completingSetup/title");
            return this;
        }

        public CompletingSetup uncheckViewReadmeFile() throws Exception
        {
            focus();
            clickOn("completingSetup/viewReadme", -52, -2);
            return this;
        }

        public CompletingSetup uncheckLaunchAlfresco() throws Exception
        {
            focus();
            clickOn("completingSetup/launchAlfresco", -94, -1);
            return this;
        }

        public CompletingSetup uncheckShowNextSteps() throws Exception
        {
            focus();
            clickOn("completingSetup/showNextSteps", -51, -1);
            return this;
        }

        public void clickFinish() throws Exception
        {
            focus();
            clickOn("completingSetup/finish");
            Utility.waitToLoopTime(WAIT_TIMEOUT);
        }
    }

    @Override
    public ACSInstaller focus() throws Exception
    {
        onSetup().focus();
        return this;
    }
}