:paw_prints:  Back to Utility [README](README.md).

---

# Change Log
All notable changes to this project will be documented in this file.

## [[v1.0.9] - TBD](/tas/alfresco-tas-tester/commits/v1.0.9)
### Added
- added constructor based on existing model
- add lastContentModel
- add Share group to TestGroup
- Reduced time from 10 seconds to 1 second for waitUntilContentIsDeleted
- added method to unset IMAP Favorite
- fix DataContent format issue on addTagToContent
- added DataBlogPost and DataCalendarEvent; Removed noderef from DataWiki
- update label from "ALL" to "WITHOUT-BUGS"
- added activity types
- add methods to delete groups and remove users from group
- added link to 1.0.9
- added skipped tests pics
- ability to filter test in html report by BUGS, by failed with no bugs
- TAS-3072 show detailed for skipped test but also if there is a test configuration
  error
- increase extent report to 2.41.2 version
- update info on OSTestMethodSelector
- Fix copy file in network drive
- update dataprep version and added 2 methods to createTaskWithProcessDef
- fix method addTaskToList
- Added method to remove user from site
- added processId field in TaskModel and refactor
- added tenantExist method that can be called from non-spring environments
- added default values to TASProperties
- Add method to verify user authorization status
- update TestGroups to include OS names (linux, windows, mac) update OSTestMethod
  to take in consideration these OS
- use small letters for testgroups
- update OSTestMethodSelector to ignore tests marked with BUGs if runBugs
  environment is set to true
- Made getFileModelWithContentSizeOfxMB static
- add current OS name where the test was executed
- Moved getFileModelWithContentSizeOfxMB from DataContent to FileModel
- 'core: added method to generate a file model with a content size of x MB'
- 'fixed: move the instantiation of list of tasks to getTasks'
- added OSTestMethodListener (ability to automatically exclude tests based
  on executor's OS type)
- fix imports on TestCountListener
- add OS_WIN, OS_UNIX groups
- 'core: Updated NetworkDrive'
- update dataprep to 1.23. Add method to set user quota
- 'core: Changed isActionExecutedOnMappedDrive access from protected to public'

## [[v1.0.8] - 2017-01-27](/tas/alfresco-tas-tester/commits/v1.0.8)
### Added
- add executeOnUnix(String command) and executeOnWin(String command)        
- added description in the report in case @Bug has one
- cleanup credentials        
- 'TestRail refactor TestRailExecutorListener to bulk update all test
  cases at the end of test suite run using #add_results_for_cases TestRail
  api method. The TestRailExecutor will now be able to: - create new test
  runs if not exists (including all or ONLY the automated test cases that
  are executed see default.properties file) - upload test cases taking in
  consideration one default RateLimit timer (defined in default.properties)
  to not overload TestRail server - more TestTypes added'
- added public static boolean isPropertyEnabled(String key) to Utility
- update dataprep to 1.22. Add method to update content and check in document        
- refactor TestRail logs
- added updateSiteVisibility method
- add methods to verify contents in trash can.
- split generic log from testrail log
- update log definition on serverHealth showTenant option
- 'fix: update method setRandomValuesForAllFields for entry case'
- 'DataUser: remove duplicated STEP'
- 'DataSite: when random name is used, added site type in name'
- set testrailexecutor log to debug status
- default alfresco.server to localhost
- add option to show or now if tenant users are visible on server
- added method to setRandomValuesForAllFields of a TestModel
- fix log info on testRailExecutorListener            
- based on TestGroup.<name> added possibility to create new sections in TestRail
  if not already created manually
- added  static ContentModel for -my- added Nodes test group fix imports on
  tenantConsole
- 'updated TestCountListener to display the tests that doesn''t have groups:
  sanity, full, core'        
- log status of mounted drive

## [[v1.0.7] - 2016-12-21](/tas/alfresco-tas-tester/commits/v1.0.7)
### Added
- CreateRandomTenant
- method to clone file model to working copy
- methods to check out and cancel check out a document
- create random wiki

### Fixed
- using current site in setIMAPFavorite
- usingResource

## [[v1.0.6] - 2016-12-19](/tas/alfresco-tas-tester/commits/v1.0.6)
### Added
- method to generate file of specific size
- ACE-2152 assertVersionIs
- ACE-2132 added listener to get the number of tests

### Removed
- models CompanyModel, NetworkModel, QuotaModel from utility to rest-api project

### Updated
- dataprep to 1.19 version. Add method to disable user
- update log4j properties showing timestamp

### Fixed
- using current site in setIMAPFavorite
- usingResource


## [[v1.0.5] - 2016-12-13](/tas/alfresco-tas-tester/commits/v1.0.5)
### Added
- ability to remove site from favorites
- remove ErrorModel from model package (moved to restapi project)
- add checkFileInPath (recursive search in directory)
- add Console operation (now you can also interact with TenantConsole, Workflow Console from admin console). You can also add new functionalities on these classes also.
- check for tenant creation on Server Health status

### Update
- jolokia writeProperty method (server property is refreshed)
- dataprep version to v1.17

## [[v1.0.4] - 2016-11-24](/tas/alfresco-tas-tester/commits/v1.0.4)
### Update
- ErrorModel needed for further RestAPI release

## [[v1.0.3] - 2016-11-24](/tas/alfresco-tas-tester/commits/v1.0.3)
### Fix
- issue related to TestRail (now we can update tests on Alfresco One Test Rail project)

### Update
- ErrorModel
- Data utilities log info

## [[v1.0.2] - 2016-11-22](/tas/alfresco-tas-tester/commits/v1.0.2)
### Added
- step on Data preparation

### Update
- ErrorModel
- TestRail integration fixes

## [[v1.0.1] - 2016-11-21](/tas/alfresco-tas-tester/commits/v1.0.1)
### Added
- DataLink.class # createRandomLink()
 
### Removed
- unnecessary plugin dependencies

### Update
- dataprep version to v1.16
- ErrorModel

## [[v1.0.0] - 2016-11-18](/tas/alfresco-tas-tester/commits/v1.0.0)
### Added

- ability to use models of Alfresco objects in your automation test
- ability to create workflows
- helpers for generating random data
- ability to create data based on XML file (site structure). See XMLTestDataProvider.java
- ability to create Users in repository
- ability to create data users

```java
@Autowired
DataUser userData;
userData.createRandomTestUser();

//or explicit specified by you:
UserModel myUser = new UserModel("tas-user", "password");
userData.createUser(myUser);
```

- ability to create Data in repository based only on alfresco IP address and admin provided

```java
@Autowired
DataContent data;
data.createFolder(FolderModel folderModel)

//where the folderModel is generated
folderModel = FolderModel.getRandomFolderModel() 

//or explicit specified by you:
folderModel = new FolderModel("myFolder")
```

- ability to create Sites in repository based only on alfresco IP address and admin provided

```java
@Autowired
DataSite data;
data.createSite(SiteModel siteModel)

//where the siteModel is generated
siteModel = SiteModel.getRandomSiteModel();

//or explicit specified by you:
siteModel = new SiteModel("myNewSite")
```

- ability to use Groups

```java
@Autowired
DataGrou dataGroup;
dataGroup.usingUser(testUser).addUserToGroup(GroupModel.getEmailContributorsGroup());
```
- ability to test extension points
If you are implementing a [custom extension](http://docs.alfresco.com/5.1/concepts/dev-platf…) point you can also test it, outside the repository.
   * just define your GET url (let's say /myExtentionTest) with one freemaker  template that will generate this xml file:
  
```xml
<?xml version="1.0" encoding="UTF-8"?>
<ExtensionPointTestSuite>
	<name>${className}</name>
	<description>${classNameDesc}</description>
	<TestCases>
	<#if testCases??>
   			<#list testCases as response>
   				<testcase name="${response.methodName?js_string}">
					<description>${response.methodNameDesc?js_string}</description>
					<duration>1</duration>
					<actualValue>${response.value}</actualValue>
					<expectedValue>${response.expectedValue}</expectedValue>
					<stackTrace>${response.stackTrace?js_string}</stackTrace>
				</testcase>
   			   <#if (response_has_next)></#if>
   		    </#list>
	</#if>
  </TestCases>
</ExtensionPointTestSuite>  
```

(take a look at [alfresco-tas-contentmodel-extension](https://gitlab.alfresco.com/tas/alfresco-tas-contentmodel-extension) implementation)

- ability of asserting if particular extension point is installed  

```java
  dataContent.assertExtensionAmpExists("<artifact-id-of-extention-point>")
```

- ability to check the server health of Alfresco installed based only on IP and admin user provided.

  ```java
  @Autowired ServerHealth serverHealth;
  ...
  serverHealth.isAlfrescoRunning() will return true if:
     * we can reach the IP provided
     * we can access the Admin System Summary Page (basic authenticating with admin provided) 
  ```

- ability to report test results in a HTML format
   * just add as listener to your TestNG test: HtmlReportListener.class

   ```
   @Listener(value=org.alfresco.utility.report.HtmlReportListener.class)
   ``` 
   
   * or add as listener to your TestNG suite xml file (approach that we embrace)
   
   ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
    <suite name="MySuite">
    	<listeners>
    		<listener class-name="org.alfresco.utility.report.HtmlReportListener"></listener>
    	</listeners>
    	(....)
    </suite> <!-- Suite -->
   ``` 

- ability to report test results into test tracker (TestRail)
   * just add annotation: @TestRail to your test (add appropiate section that already exist in TestRail)
   * just add as listener to your TestNG test: TestRailExecutorListener.clas
   
   ```
   @Listener(value=org.alfresco.utility.testrail.TestRailExecutorListener.class)
   ``` 
   
   * or add as listener to your TestNG suite xml file (approach that we embrace)

   ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
    <suite name="MySuite">
    	<listeners>
    		<listener class-name="org.alfresco.utility.testrail.TestRailExecutorListener.class"></listener>
    	</listeners>
    	(....)
    </suite> <!-- Suite -->
   ``` 
- ability to document steps in your @Test using STEP helper (info are passed on test tracker too)   
- ability to annotate issues in your tests

   ```java
   @Bug(id="ACE-1")
   @Test
   public void myTest()
   {
    (...)
   }

   ```

  (this issue will be visible in HTML report (if HtmlReportListener is used) or in TestRail (issue added as Defect) if TestRailExecutorListener is used)

- ability to call JMX on Alfresco server (if alfresco is running in docker or behind a firewall)

  We take advantage of https://jolokia.org agent to interact with JMX calls on server side. 
  Docker provisioning will automatically installed Jolokia agent on each Stack, so we can use this instead of direct JMX calls.

  This feature is configurable from default.properties file (just enable of disable it)

  ```
   # in containers we cannot access directly JMX, so we will use http://jolokia.org agent
   # disabling this we will use direct JMX calls to server
   jmx.useJolokiaAgent=true
  ```
  In the code you will use something like:

  ```java
    @Autowired
    JmxBuilder jmx;
    ...
    jmx.getJmxClient().readProperty("Alfresco:Name=DatabaseInformation", "DriverName")
  ```
- ability to define your alfresco test environment properties (server, credentials, etc.)  
- ability to query DBs