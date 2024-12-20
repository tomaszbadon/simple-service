podTemplate(
  agentContainer: 'maven',
  agentInjection: true,
  containers: [
    containerTemplate(name: 'maven', image: 'maven:3.9.9-eclipse-temurin-17'),
    containerTemplate(name: 'golang', image: 'golang:1.16.5', command: 'sleep', args: '99d')
  ]) {

    node(POD_LABEL) {
        stage('Get a Maven project') {
            git 'https://github.com/tomaszbadon/simple-service.git'
            container('maven') {
                stage('Build a Maven project') {
                    sh './gradlew clean build -x test  --no-daemon'
                }
            }
        }
    }
}
