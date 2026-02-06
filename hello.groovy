pipeline {
    agent any

    environment {
        IMAGE_NAME = "fdx-webapp"
        IMAGE_TAG  = "1.0"
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
                sh 'docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .'
            }
        }
    }

    post {
        success {
            echo "Docker image ${IMAGE_NAME}:${IMAGE_TAG} built successfully"
        }
    }
}
