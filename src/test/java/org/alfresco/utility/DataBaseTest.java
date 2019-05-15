package org.alfresco.utility;

import junit.framework.Assert;

import org.alfresco.utility.network.db.DatabaseOperationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration("classpath:alfresco-tester-context.xml")
public class DataBaseTest extends AbstractTestNGSpringContextTests
{

    @Autowired
    DatabaseOperationImpl databaseOpImpl;

    @Test
    public void testDataBaseConnect() throws Exception
    {
        Assert.assertTrue("Database connection is open", databaseOpImpl.connect());
    }

    @Test
    public void testDataBaseDisconect() throws Exception
    {
        Assert.assertTrue("Database connection is close", databaseOpImpl.disconect());
    }

    @Test
    public void testDataBaseQuery() throws Exception
    {
        String query = "SELECT * FROM alf_content_url WHERE id=1;";
        Assert.assertEquals(1, databaseOpImpl.executeQuery(query).size());
    }
}
