properties([
    	buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '1', numToKeepStr: '3'))
    ])

node('paul-test') {     
    configFileProvider([configFile(fileId: 'alfresco-maven', variable: 'MAVEN_SETTINGS')]) {

        stage('Checkout') {
            bat 'git config --global http.sslVerify false'
            checkout scm
        }

        stage('Compile') {
            bat 'mvn -s %MAVEN_SETTINGS% -U clean install -DskipTests'
        }
    }
}