pipeline {

    agent {
        docker { image 'openjdk:17-ea-alpine' }
    }

    stages {

        stage("build") {
            steps {
                sh './gradlew clean build -x test  --no-daemon'
            }
        }

    }
}
