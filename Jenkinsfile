  podTemplate(
    agentContainer: 'openjdk',
    agentInjection: true,
    containers: [
      containerTemplate(name: 'openjdk', image: 'openjdk:17-alpine'),
    ]) {
  
      node(POD_LABEL) {
        pipeline {
          stages{
          stage('Building Simple Service') {
              container('openjdk') {
                  stage('Prepare Container') {
                      echo 'Hello World!'
                      sh '''
                        sleep 5m
                      '''
                  }
              }
          }
          }
        }
      }
  }
