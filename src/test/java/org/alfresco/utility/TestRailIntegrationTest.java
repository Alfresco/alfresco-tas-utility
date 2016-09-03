package org.alfresco.utility;

import org.alfresco.utility.testrail.TestRailTestNGTestListener;
import org.alfresco.utility.testrail.TestType;
import org.alfresco.utility.testrail.annotation.TestRail;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@ContextConfiguration("classpath:alfresco-tester-context.xml")
@Listeners(value = TestRailTestNGTestListener.class)
public class TestRailIntegrationTest extends AbstractTestNGSpringContextTests
{
    @Test
    @TestRail(type = TestType.FUNCTIONAL, section = { "people", "nodes" })
    public void t1()
    {

    }

    @TestRail(section = { "rest-api", "comments", "nodes" }, description = "")
    @Test
    public void adminShouldCreateComments2()
    {
        System.out.println("AAA");
    }
}
