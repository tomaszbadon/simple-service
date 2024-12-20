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
                    
                    volumes {
                        secretVolume {
                            name 'my-secret'
                            secretName 'my-secret-name' 
                        }
                    }
                }
            
            steps {
                sh './gradlew clean build -x test  --no-daemon'
            }
        }

    }
}
