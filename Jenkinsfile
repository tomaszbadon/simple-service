pipeline {

    agent { docker 'gradle:8.11.1-jdk17-graal' }

    stages {

        stage("test") {
            steps {
                sh './gradlew clean test --no-daemon'
            }
        }

    }
}