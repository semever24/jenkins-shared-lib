def call(Map config) {
    withCredentials([string(credentialsId: 'dockerhub-password', variable: 'DOCKER_PASS')]) {
        sh """
        docker login -u ${env.DOCKER_USER} -p $DOCKER_PASS
        docker build -t ${config.image} .
        docker push ${config.image}
        """
    }
}