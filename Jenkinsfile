pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                echo 'Code checkout done by multibranch'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
    }
}

