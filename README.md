## Synopsis

This is the home of the **TAS**( **T**est **A**utomation **S**ystem)- **Utility** project.
It is based on Apache Maven, compatible with major IDEs and is using also Spring capabilities for dependency injection.

As a high level overview, this project contains a couple of functionalities usefull for automation testing as: 
* reading/defining test environment settings (e.g. alfresco server details, authentication, etc.)
* utilities (creating files,folders)
* test data generators (for site, users, content, etc)
* helpers (i.e. randomizers, test environment information)
* test reporting capabilities
* test management integration (at this point we support integration with [Test Rail](https://alfresco.testrail.net) (v5.2.1)
* Healthchecks (check if server is reachable, if server is online)
* Generic Internal-DSL (Domain Specific Language)

Using a centralized location (Nexus), everyone will be able to reuse this individual interfaces in their own projects, adding new functionalities, extending also the automation core functionalities - that will be shared across teams. 


## Prerequisite 
(tested on unix/non-unix destribution)
* [Java SE 1.8](http://www.oracle.com/technetwork/java/javase/downloads/index.html).
* [Maven 3.3](https://maven.apache.org/download.cgi) installed and configure according to [Windows OS](https://maven.apache.org/guides/getting-started/windows-prerequisites.html) or [Mac OS](https://maven.apache.org/install.html).
* Your favorite IDE as [Eclipse](https://eclipse.org/downloads/) or [InteliJ](https://www.jetbrains.com/idea).
* Access to [Nexus](https://nexus.alfresco.com/nexus/) repository.
* Access to Gitlab [TAS](https://gitlab.alfresco.com/tas/) repository.
* GitLab client for your operating system. (we reommend [SourceTree](https://www.sourcetreeapp.com)).
* Getting familiar with [Basic Git Commands](http://docs.gitlab.com/ee/gitlab-basics/basic-git-commands.html).
* Getting familiar with [Maven](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html).
* Getting familiar with [Spring](http://docs.spring.io).

## Installation (if you want to contribute)

* Open your Gitlab client and clone the repository of this project.
* You can do this also from command line (or in your terminal) adding

```bash
> git clone https://gitlab.alfresco.com/tas/alfresco-tas-tester.git
```

* Install and check if all dependencies are downloaded

```ruby
> cd alfresco-tas-tester
> mvn clean install -DskipTests
# you should see one [INFO] BUILD SUCCESS message displayed
```

## Package Presentation

This project uses a simple maven project [archetype](https://maven.apache.org/plugins-archives/maven-archetype-plugin-1.0-alpha-7/examples/simple.html):
```python
├── src
│   ├── main
│   │   ├── java
│   │   │   └── org
│   │   │       └── alfresco
│   │   │           └── utility
│   │   │               ├── data # helpers for creating Sites/Files, Users, etc)
│   │   │               │   (...)
│   │   │               ├── dsl
│   │   │               │   ├──(...)
│   │   │               ├── exception # custom exception
│   │   │               │   (...)
│   │   │               ├── model #modeling generic objects that will be reused in test
│   │   │               │   ├── FileModel.java
│   │   │               │   ├── FileType.java
│   │   │               │   ├── FolderModel.java
│   │   │               │   └── UserModel.java
│   │   │               │   └── (...)
│   │   │               ├── network # network based helpers
│   │   │               │   └── (...)
│   │   │               ├── report #handling reporting (i.e. listeners for generating html reports)
│   │   │               │   └── (...)
│   │   │               └── testrail # TestRail integration utils
│   │   │               │   └── (...)
│   │   └── resources
│   └── test
│       ├── java
│       │   └── org
│       │       └── alfresco
│       │           └── utility #testing classes/sample code
│       │               ├── (...)
│       └── resources
│           ├── default.properties #one place where you defined all settings
│           ├── log4j.properties
│           ├── testdata #placeholder for holding test data
│           │   └── (...)
```

## Sample Usage
In your maven project, in your pom.xml file add the following dependency
```
<dependency>
			<groupId>org.alfresco.tas</groupId>
			<artifactId>utility</artifactId>
			<version></version>
</dependency>
```
(where ${tas.utility.version} is the latest verion released on [Nexus](https://nexus.alfresco.com/nexus/).)

### Configure your maven project to use tas.utility
* if you have one [simple maven project](https://maven.apache.org/plugins-archives/maven-archetype-plugin-1.0-alpha-7/examples/simple.html) created, you must add Spring bean capabilities to interact with tas.utility project
	* add dependency to your pom.xml (as indicated above) - _no need for spring bean dependencies, this are downloaded automatically from tas.utilit_	
	* import resources in src/test/resources/<your-test-context.xml>
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
	xmlns:mongo="http://www.springframework.org/schema/data/mongo"
	xsi:schemaLocation="http://www.springframework.org/schema/context
          http://www.springframework.org/schema/context/spring-context-3.0.xsd
          http://www.springframework.org/schema/data/mongo
          http://www.springframework.org/schema/data/mongo/spring-mongo-1.0.xsd
          http://www.springframework.org/schema/beans
          http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">

	<context:annotation-config />
	<context:component-scan base-package="org.alfresco" />

	<import resource="classpath:dataprep-context.xml" />
	<import resource="classpath*:alfresco-tester-context.xml" />
</beans>
```

* copy [default.properties](src/test/resources/default.properties) to your src/test/resources folder, updating the settings as you want.
* create a simple TestNG test for testing the autowired bean capabilities
	```java
    @ContextConfiguration("classpath:alfresco-smtp-context.xml")
    public abstract class MyTestIsAwesome extends AbstractTestNGSpringContextTests{
    	@Autowired
	    protected ServerHealth serverHealth;

    	@Test
	    public void checkAlfrescoServerIsOnline() throws Exception
    	{
        	serverHealth.assertServerIsOnline();
    	}
    }
	```
* running testNG test your test should pass if settings from default.properties are properly set.

**_NOTE_:** _we initialized our utility using the [@Autowired](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/beans/factory/annotation/Autowired.html) spring annotation. This utility will know automatically to read and interpret the settings defind in your project._

### How to create new data (files/folder)
* configure your project to use spring (as highlighted above)
* in your test file add:
  ```java
  @Autowired
  protected DataContent dataContent;

  @Test
  public void creatingFolderInRepoLocation()
  {
  /*
  *this call will create folder  'myTest' under '/Sites/mySite/documentLibrary' location
  * using default admin user specified in default.properties file
  */
  dataContent.usingSite("mySite").createFolder("myTest")

  /*
  *this call will create folder 'myTest2' under '/' root folder
  * using default admin user specified in default.properties file
  */
  dataContent.usingRoot().createFolder("myTest2")
  }

  @Test
  public void creatingFolderInRepoLocationWithCustomUsers()
  {
  /*
  *this call will create folder  'myTest' under '/Sites/mySite/documentLibrary' location
  * using default user'testUser'
  */
  dataContent.usingUser(testUser).usingSite("mySite").createFolder("myFolderWithUser")

  /*
  *this call will create folder 'myTest2' under '/' root folder
  * using user testUser
  */
  dataContent.usingUser(testUser).usingRoot().createFolder("myFolderWithUser")
  }
  ```

* remember models defined above in the package presentation ? Those models can be used in the utilities presented above. So 'testUser' for example is a UserModel class that can be defined as:
  ```java
  UserModel testUser = new UserModel("testUserName", "testUserPassword")
  ```
  We also have some generators that will ease your code style:
  ```java
  // this will create a new UserModel class using the default admin user defined in default.properties file
  UserModel testUser = dataContent.getAdminUser();
  ```

  ```java
  // this will create a new random user in repository and return a new UserModel class
  @Autowired
  DataUser dataUser;
  //(...)
  UserModel testUser = dataUser.createRandomTestUser();
  ```
### How to create a new site
* configure your project to use spring (as highlighted above)
* in your test file add:
  ```java
  @Autowired
  protected DataSite dataSite
  
  @Test
  public void createSite()
  {
  	// this will create a new public random site using admin (no user provided as we see bellow)
  	dataSite.createPublicRandomSite()
    UserModel testUser = dataUser.createRandomTestUser();
    dataSite.createPublicRandomSite()
  }
  ```

## Reference

TBD


## Contributors

As contributors and maintainers of this project, we pledge to respect all people who contribute through reporting issues, posting feature requests, updating documentation, submitting pull requests or patches, and other... [more](CODE_OF_CONDUCT.md)

## License

TBD