pipeline {

    agent any
    
    stages {

        stage("build") {

            steps {
                kubernetesPodTemplate {
                    // Your pod template configuration here
                    containers {
                        containerTemplate(name: 'myContainer', image: 'openjdk:17')
                    }
                }
            }
            
            steps {
                sh './gradlew clean build -x test  --no-daemon'
            }
        }

    }
}
