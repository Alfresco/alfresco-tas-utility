package org.alfresco.utility;

import org.alfresco.utility.testrail.TestType;
import org.alfresco.utility.testrail.annotation.TestRail;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@ContextConfiguration("classpath:alfresco-tester-context.xml")
public class TestRailSampleTest extends AbstractTestNGSpringContextTests
{
    @Test
    @TestRail(type = TestType.FUNCTIONAL, section = { "rest-api", "comments" })
    public void myRestTestOnCommentsThatIsFailing()
    {
        Assert.assertEquals("TEST", "TEST1");
    }

    @Test
    @TestRail(type = TestType.AUTOMATED, section = { "rest-api", "files" })
    public void myRestTestOnFiles()
    {
    }

    @Test
    @TestRail(section = { "cmis", "files" })
    public void myCMISTestOnFiles()
    {
    }

    @Test
    @TestRail(section = { "cmis", "folders" })
    public void myCMISTestOnFolders()
    {
    }

    //
    // @Override
    // @BeforeMethod
    // protected void springTestContextPrepareTestInstance() throws Exception {
    // super.springTestContextPrepareTestInstance();
    // }
}
