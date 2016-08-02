package org.alfresco.tester.data;

import org.alfresco.tester.ServerProperties;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class TestData {
	@Autowired
	protected ServerProperties properties;

	protected static String PASSWORD = "password";
	protected static String EMAIL = "%s@tas-automation.org";

	public static String getRandomAlphanumeric() {
		return RandomStringUtils.randomAlphabetic(10);
	}
}
