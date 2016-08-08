package org.alfresco.tester.data;

import org.alfresco.tester.LogFactory;
import org.alfresco.tester.TasProperties;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class TestData
{

    static Logger LOG = LogFactory.getLogger();
    @Autowired
    protected TasProperties properties;

    protected static String PASSWORD = "password";
    protected static String EMAIL = "%s@tas-automation.org";
    protected static String USER = "user";

    public static String getRandomAlphanumeric()
    {
        String value = RandomStringUtils.randomAlphabetic(10);
        LOG.info("Generating alphanumeric string: {}", value);
        return value;
    }
}
