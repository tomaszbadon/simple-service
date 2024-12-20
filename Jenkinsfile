podTemplate(
  agentContainer: 'ubuntu',
  agentInjection: true,
  containers: [
    containerTemplate(name: 'ubuntu', image: 'ubuntu:latest'),
  ]) {

    node(POD_LABEL) {
        stage('Building Simple Service') {
            container('ubuntu') {
                stage('Prepare Container') {
                    echo 'Hello World!'
                }
            }
        }
    }
}
