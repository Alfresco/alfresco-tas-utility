package org.alfresco.utility.application.gui.installer;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import static org.alfresco.utility.report.log.Step.STEP;

/**
 * Created by Claudia Agache on 7/10/2017.
 */
public class InstallerEndToEndTests extends InstallerTest
{
    @Autowired
    ACSUninstaller uninstaller;

    @Autowired
    ComponentsVersion components;

    public void installationWithDefaultParameters()
    {
        // TODO
    }

    /**
     * AONE-18285
     */
//    @Test()
    public void installationInAdvancedMode() throws Exception
    {
        STEP("1. Precondition: Alfresco One installer is running - Installation Type form is opened.");
        navigateToInstallationTypeForm();

        STEP("2. At the 'Installation Type' from select 'Advanced Mode' radio button and click 'Forward'.");
        installer.onInstallationTypePage().chooseAdvancedInstall();
        installer.onSetup().clickNext();

        STEP("3. At the 'Select Components' form click 'Forward' button.");
        installer.onSelectComponentsPage()
                .assertJavaIsChecked()
                .assertPostgreSQLIsChecked()
                .assertLibreOfficeIsChecked()
                .assertAlfrescoContentServicesIsCheckedAndDisabled()
                .assertSolr1IsUnchecked()
                .assertSolr4IsChecked()
                .assertAlfrescoOfficeServicesIsChecked()
                .assertWebQuickStartIsUnchecked()
                .assertGoogleDocsIntegrationIsChecked();
        installer.onSetup().clickNext();

        STEP("4. At the 'Installation Folder' form leave default value and click 'Forward' button.");
        installer.onInstallationFolderPage().assertInstallationFolderIs("");
        installer.onSetup().clickNext();

        STEP("5. At the 'Database Server Parameters' form click 'Forward' button.");
        installer.onDatabaseParametersPage().assertDatabasePortIs("");
        installer.onSetup().clickNext();

        STEP("6. At the 'Tomcat Port Configuration' form click 'Forward' button. ");
        installer.onTomcatPortConfigurationPage();
        //TODO It contains next fields with default values:

//        Web Server Domain - 127.0.0.1
//        Tomcat Server Port - 8080
//        Tomcat Shutdown Port - 8005
//        Tomcat SSL Port - 8443
//        Tomcat AJP Port - 8009
        installer.onSetup().clickNext();

        STEP("7. At the 'LibreOffice Server Port' form click 'Forward' button.");
        installer.onLibreOfficeServerPortPage();
        //TODO It contains "LibreOffice Server Port" field with default value 8100.

        installer.onSetup().clickNext();

        STEP("8. At the 'Sharded Solr installation' form click 'Forward' button.");
        installer.onShardedSolrInstallationPage();
        //TODO assert No is checked by default.
        installer.onSetup().clickNext();

        STEP("9. At the 'Alfresco FTP Port' form click 'Forward' button.");
        installer.onFtpPortPage();
        //TODO It contains "Port" field with default value 21.
        installer.onSetup().clickNext();

        STEP("10. At the 'Alfresco RMI Port' form click 'Forward' button.");
        installer.onRmiPortPage();
        //TODO It contains "Port" field with default value 50500.
        installer.onSetup().clickNext();

        STEP("11. At the 'Admin Password' form type password for admin (e.g. admin), confirm it and click 'Forward' button.");
        installer.onAdminPasswordPage()
                .setAdminPassword(installer.installerProperties.getAdminPassword())
                .setRepeatAdminPassword(installer.installerProperties.getAdminPassword());
        installer.onSetup().clickNext();

        STEP("12. At the 'Install as a service' form click 'Forward' button.");
        //TODO assert YES is checked by default.
        installer.onSetup().clickNext();

        STEP("13. At the 'Ready to Install' form click 'Forward' button. ");
        installer.onSetup().clickNext();

        STEP("14. At the 'Completing the Alfresco One Setup Wizard' form click 'Finish' button.");
        installer.onCompletingSetupPage().clickFinish();
        //TODO check Readme file is displayed, Alfresco is started successfully. Alfresco One Share page is displayed.

    }

    @Test(groups = { "demo" }, priority = 0) public void verifyingComponentsVersion() throws Exception
    {
        STEP("1. Go to /opt/alfresco-one/java/bin/ and verify JRE version");
        components.assertJREVersionIs("java version \"1.8.0_");
        STEP("2. Go to /opt/alfresco-one/tomcat/bin/ and verify Tomcat version");
        components.assertTomcatVersionIs("Apache Tomcat/7.0.59");
        STEP("3. Go to tomcat install folder (/opt/alfresco-one/tomcat/lib) and check JDBC");
        components.assertJDBCIs("postgresql-9.4.1211.jre7.jar");
        STEP("4. In console go to install folder of postgresql and get psql version");
        components.assertPsqlVersionIs("psql (PostgreSQL) 9.4.4");
        STEP("5 Verify ImageMagick version.");
        components.assertImageMagickVersionIs("ImageMagick 7.0.5");
    }

    @Test(groups = { "demo" }, priority = 1) public void uninstallAlfrescoOne() throws Exception
    {
        STEP("1. Run uninstall from Alfresco installation directory.");
        uninstaller.open();
        uninstaller.waitForUninstallerToOpen();
        STEP("2. At the 'Question' form click 'No' button.");
        uninstaller.onQuestionDialog().clickNo();
        uninstaller.assertDialogIsClosed();
        STEP("3. Run uninstaller again");
        uninstaller.open();
        uninstaller.waitForUninstallerToOpen();
        STEP("4. At the 'Question' form click 'Yes' button.");
        uninstaller.onQuestionDialog().clickYes();
        uninstaller.waitForConfirmationDialog();
        STEP("5. When uninstalling completed check Alfresco installation folder.");
        uninstaller.assertInstallationFolderIsEmpty();
    }
}
