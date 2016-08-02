package org.alfresco.tester;

import org.apache.chemistry.opencmis.commons.impl.UrlBuilder;
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
public class ServerProperties {
	@Autowired
	Environment env;

	@Value("${admin.user}")
	private String adminUserName;

	@Value("${admin.password}")
	private String adminPassword;

	@Value("${alfresco.scheme}")
	private String scheme;

	@Value("${alfresco.server}")
	private String server;

	@Value("${alfresco.port}")
	private int port;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
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

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return Base URL of Test Server
	 */
	public String getBaseURL() {
		return new UrlBuilder(getScheme(), getServer(), getPort(), "").toString();
	}

}
