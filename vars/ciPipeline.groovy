def call(Map config) {

    pipeline {
        agent any

        environment {
            IMAGE_TAG = "${BUILD_NUMBER}"
        }

        stages {

            stage('Initialize') {
                steps {
                    script {
                        env.APP_NAME    = config.appName
                        env.DOCKER_USER = config.dockerHubUser
                        env.IMAGE_NAME  = "${env.DOCKER_USER}/${env.APP_NAME}:${env.IMAGE_TAG}"
                    }
                }
            }

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
                    dockerBuild(image: env.IMAGE_NAME)
                }
            }

            stage('Deploy to Kubernetes') {
                steps {
                    k8sDeploy(image: env.IMAGE_NAME)
                }
            }
        }

        post {
            success {
                echo "Deployment successful 🚀"
                echo "Deployed Image: ${env.IMAGE_NAME}"
            }
        }
    }
}