pipeline {

    agent any

    stages {

        stage("test") {
            steps {
                sh './gradlew clean test --no-daemon'
            }
        }

    }
}