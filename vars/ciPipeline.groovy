def call(Map config) {
    pipeline {
        agent any

        environment {
            APP_NAME   = config.appName
            DOCKER_USER = config.dockerHubUser
            IMAGE_TAG  = "${BUILD_NUMBER}"
            IMAGE_NAME = "${DOCKER_USER}/${APP_NAME}:${IMAGE_TAG}"
        }

        stages {
            stage('Checkout') {
                steps {
                    checkout scm
                }
            }

            stage('Build & Test') {
                steps {
                    sh 'mvn clean test package'
                }
            }

            stage('Docker Build & Push') {
                steps {
                    dockerBuild(image: IMAGE_NAME)
                }
            }

            stage('Deploy to Kubernetes') {
                steps {
                    k8sDeploy(image: IMAGE_NAME)
                }
            }
        }

        post {
            success {
                echo "Deployment successful 🚀"
            }
        }
    }
}