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
        //TODO
    }

    public void installationInAdvancedMode()
    {
        //TODO
    }

    @Test(groups={"demo"}, priority=0)
    public void verifyingComponentsVersion() throws Exception {
        STEP ("1. Go to /opt/alfresco-one/java/bin/ and verify JRE version");
        components.assertJREVersionIs("java version \"1.8.0_");
        STEP ("2. Go to /opt/alfresco-one/tomcat/bin/ and verify Tomcat version");
        //components.assertTomcatVersionIs("Apache Tomcat/7.0.59");
        STEP ("3. Go to tomcat install folder (/opt/alfresco-one/tomcat/lib) and check JDBC");
        components.assertJDBCIs("postgresql-9.4.1211.jre7.jar");
        STEP ("4. In console go to install folder of postgresql and get psql version");
        components.assertPsqlVersionIs("psql (PostgreSQL) 9.4.4");
        STEP ("5 Verify ImageMagick version.");
        components.assertImageMagickVersionIs("ImageMagick 7.0.5");
    }

    @Test(groups={"demo"}, priority=1)
    public void uninstallAlfrescoOne() throws Exception {
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
