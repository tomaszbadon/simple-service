pipeline {
    agent none
    
    stages {
        stage('Run in Kubernetes') {
            steps {
                kubernetesPodTemplate {
                    // Your pod template configuration here
                    containers {
                        containerTemplate(name: 'myContainer', image: 'openjdk:17')
                    }
                }
                
                steps {
                    sh './gradlew clean build -x test  --no-daemon'
                }
            }
        }
    }
}
