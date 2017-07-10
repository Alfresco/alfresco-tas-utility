package org.alfresco.utility.application.gui.installer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

/**
 * Created by Claudia Agache on 7/10/2017.
 */
@ContextConfiguration("classpath:alfresco-tester-context.xml")
public abstract class InstallerTest extends AbstractTestNGSpringContextTests
{
    @Autowired
    ACSInstaller installer;
}
