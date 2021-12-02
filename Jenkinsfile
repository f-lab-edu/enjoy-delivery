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


        stage('backend build') {
            steps {
                sh "pwd"
                sh "gradle clean build -s"
            }
        }

        stage('SSH transfer') {
            steps {
                sshPublisher(
                continueOnError: false, failOnError: true,
                publishers: [
                    sshPublisherDesc(
                     configName: "ncloud-server1",
                     verbose: true,
                     transfers: [
                        sshTransfer(
                        sourceFiles: "build/libs/enjoy-delivery.jar, deploy.sh",
                        remoteDirectory: "project"
                        )
                    ])
                ])
            }
        }

        stage('server deploy') {
            steps {
                sh "chmod +x ./deploy.sh"
                sh "./deploy.sh"
            }
        }


    }
}