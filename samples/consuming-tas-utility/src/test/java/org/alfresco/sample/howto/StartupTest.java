package org.alfresco.sample.howto;

import org.alfresco.utility.LogFactory;
import org.alfresco.utility.data.DataUser;
import org.alfresco.utility.exception.DataPreparationException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration("classpath:alfresco-test-context.xml")
public class StartupTest extends AbstractTestNGSpringContextTests {
	Logger LOG = LogFactory.getLogger();
	@Autowired
	DataUser dataUser;

	@Test
	public void start() throws DataPreparationException {	
		LOG.info("Creating a random user: {}", dataUser.usingAdmin().createRandomTestUser());
	}
}
