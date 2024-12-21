pipeline {
  agent {
    kubernetes {
      yaml '''
        apiVersion: v1
        kind: Pod
        metadata:
          labels:
            some-label: some-label-value
        spec:
          containers:
          - name: alpine
            image: alpine-openjdk17
            command:
            - cat
            tty: true
        '''
      retries 2
    }
  }
  stages {
    stage('Build Simple-Service') {
      steps {
        container('alpine') {
          sh './gradlew clean build -x test  --no-daemon'
        }
      }
    }
  }
}
