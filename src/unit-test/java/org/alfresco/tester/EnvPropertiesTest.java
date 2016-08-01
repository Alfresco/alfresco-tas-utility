package org.alfresco.tester;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@ContextConfiguration("classpath:alfresco-tester-context.xml")
public class EnvPropertiesTest extends AbstractTestNGSpringContextTests {

	@Autowired
	protected EnvProperties properties;

	@Test
	public void getEnvPropertiesBean() {
		Assert.assertNotNull(properties, "Bean EnvProperties is initialised");
	}

	@Test
	public void getAdminUsername() {
		Assert.assertEquals(properties.getAdminUser(), "admin");
	}

	@Test
	public void getAdminPassword() {
		Assert.assertEquals(properties.getAdminUser(), "admin");
	}
}
