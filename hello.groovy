pipeline {
    agent any

    environment {
        IMAGE_NAME = "fdx-webapp"
        IMAGE_TAG  = "${BUILD_NUMBER}"
        DOCKERHUB_USER = "thippeshanaik"
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/ThippeshaNaik/fdx-ui-backend-project-2026.git'
            }
        }

        stage('Build WAR') {
            steps {
                dir('fdx-backend') {
                    sh 'mvn clean package'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                  docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .
                '''
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'docker-hub-id',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh '''
                      docker login -u $DOCKER_USER -p $DOCKER_PASS
                      docker tag ${IMAGE_NAME}:${IMAGE_TAG} $DOCKER_USER/${IMAGE_NAME}:${IMAGE_TAG}
                      docker push $DOCKER_USER/${IMAGE_NAME}:${IMAGE_TAG}
                    '''
                }
            }
        }
    }

    post {
        success {
            echo "Docker image pushed: ${DOCKERHUB_USER}/${IMAGE_NAME}:${IMAGE_TAG}"
        }
    }
}
