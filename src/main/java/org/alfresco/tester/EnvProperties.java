package org.alfresco.tester;

import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

/**
 * Generic Properties class that will load and override properties based on
 * environment defined For "test" environment just define {code}
 * System.setProperty("environment", "test"); {code} or run your test passing
 * -Denvironment=test
 * 
 * @author Paul.Brodner
 */
@Configuration
@PropertySource("classpath:default.properties")
@PropertySource(value = "classpath:${environment}.properties", ignoreResourceNotFound = true)
public class EnvProperties {
	@Autowired
	Environment env;

	@Value("${alfresco.url}")
	private URL alfrescoUrl;

	@Value("${admin.user}")
	private String adminUserName;

	@Value("${admin.password}")
	private String adminPassword;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	public Environment getEnv() {
		return env;
	}

	public String getAdminUser() {
		return adminUserName;
	}

	public void setAdminUserName(String adminUserName) {
		this.adminUserName = adminUserName;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public int getAlfrescoPort() {

		return alfrescoUrl.getPort();
	}

	public String getHost() {
		return alfrescoUrl.getHost();
	}

}
