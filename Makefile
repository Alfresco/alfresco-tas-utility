bamboo_JAVA_HOME 	?= /opt/jdk-11 # default to java11 if not set

ifeq ($(SERVER),)
	SERVER := cd acs && docker-compose
endif

ifeq ($(MVN),)
	ifeq ($(bamboo_working_directory),) 
		MVN	:= mvn
	else 
		# on bamboo environment define the environment
		export M2_HOME=
		MVN:=/opt/maven-3.3/bin/mvn
		export JAVA_HOME=$(bamboo_JAVA_HOME)
	endif	
endif

export GIT_AUTHOR_NAME=bamboo_auth
export GIT_AUTHOR_EMAIL=bamboo_auth@internal.alfresco.com
export GIT_COMMITTER_NAME=bamboo_auth
export GIT_COMMITTER_EMAIL=bamboo_auth@internal.alfresco.com

compile:
	$(MVN) --version && $(MVN) --batch-mode compile -U -DskipTests

release: ## perform the release, automatically increase the version
	$(MVN) --batch-mode release:prepare release:perform \
	-Dmaven.javadoc.skip=true \
	-Dresume=false \
	-Dusername=$(GIT_COMMITTER_NAME) \
	-Dpassword=$(bamboo_auth_ldap_password) \
	"-Darguments=-Dmaven.test.skip=true -Dmaven.javadoc.skip=true -Dadditionalparam=-Xdoclint:none"
