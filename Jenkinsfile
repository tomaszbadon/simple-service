pipeline {

    agent any
    
    stages {

        stage("build") {
            steps {
                sh './gradlew clean build -x test  --no-daemon'
            }
        }

    }
}
