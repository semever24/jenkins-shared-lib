def call(Map config) {
    sh """
    sed -i 's|IMAGE_PLACEHOLDER|${config.image}|g' k8s/deployment.yaml
    kubectl apply -f k8s/deployment.yaml
    kubectl apply -f k8s/service.yaml
    """
}