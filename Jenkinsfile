pipeline {
    agent any
    tools {
      gradle 'Gradle 7.2'
    }
    options {
        timeout(time: 1, unit: 'HOURS')
    }
    environment {
        SOURCECODE_JENKINS_CREDENTIAL_ID = 'meme2367'
        SOURCE_CODE_URL = 'https://github.com/f-lab-edu/enjoy-delivery/'
        RELEASE_BRANCH = 'develop'
    }
    stages {


        stage('clone') {
            steps {
                git url: "$SOURCE_CODE_URL",
                    branch: "$RELEASE_BRANCH",
                    credentialsId: "$SOURCECODE_JENKINS_CREDENTIAL_ID"
                sh "ls -al"
            }
        }

        stage('backend dockerizing') {
            steps {
                sh "pwd"

                sh "gradle clean build -s"

                sh "docker build -t ed ."

            }
        }

        stage('deploy') {
            steps {
                sh "docker run -d -p 8080:8080 ed"
            }
        }
    }
}