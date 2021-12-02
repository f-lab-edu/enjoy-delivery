def ssh_publisher(SERVER_CONFIG) {
    sshPublisher(
        continueOnError: false,
        failOnError: true,
        publishers:[
            sshPublisherDesc(
                configName: "${SERVER_CONFIG}",
                verbose: true,
                transfers: [
                    sshTransfer(
                        sourceFiles: "build/libs/enjoy-delivery.jar, deploy.sh",
                        remoteDirectory: "project"
                    )
                ]
            )
        ]
    )
}

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
        SERVER_LIST = 'server1,server2'
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
                sh "gradle clean test"
                echo "build.."
                sh "gradle build -s"
            }
        }

        stage('server deploy') {
            steps {
              echo "deploy.."
              echo "${SERVER_LIST}"

              script {
                SERVER_LIST.tokenize(',').each {
                  echo "SERVER: ${it}"
                  ssh_publisher("${it}")
                  sh "chmod +x ./deploy.sh"
                  sh "./deploy.sh"
                }
              }
            }
        }
    }
}