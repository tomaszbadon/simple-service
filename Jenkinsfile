  podTemplate(
    agentContainer: 'openjdk',
    agentInjection: true,
    containers: [
      containerTemplate(name: 'openjdk', image: 'openjdk:17'),
    ]) {
  
      node(POD_LABEL) {

          stage('Building Simple Service') {
              container('openjdk') {
                  stage('Prepare Container') {
                      echo 'Hello World!'
                      sh 'git https://github.com/tomaszbadon/simple-service.git'
                      cd 'simple-service'
                  }
              }
           }
      }
  }
