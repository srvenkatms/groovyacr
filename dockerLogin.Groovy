// dockerLogin.groovy

def loginToDockerRegistry(acrName, acrUsername, acrPassword) {
    def acrLoginServer = "${acrName}.azurecr.io"
    echo "Logging in to Azure Container Registry: ${acrLoginServer} using ACR credentials."
    // Login to ACR using username and password
    sh "echo ${acrPassword} | docker login ${acrLoginServer} -u ${acrUsername} --password-stdin"
}

return this
