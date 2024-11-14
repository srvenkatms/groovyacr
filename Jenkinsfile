pipeline {
    agent any

    environment {
    ACR_NAME = 'jtogh'         // Replace with your Azure Container Registry name
    ACR_LOGIN_SERVER = "${ACR_NAME}.azurecr.io"
    DOCKER_IMAGE_NAME = 'helloworld'   // Replace with your desired image name
    DOCKER_IMAGE_TAG = 'latest'        // Replace with your desired image tag
    AZURE_CLIENT_ID = credentials('AZURE_CLIENT_ID')
    AZURE_CLIENT_SECRET = credentials('AZURE_CLIENT_SECRET')
    AZURE_TENANT_ID = credentials('AZURE_TENANT_ID')
    AZURE_SUBSCRIPTION_ID = 'ac616a3b-53be-4cbf-961c-5467b1590718' //credentials('AZURE_SUBSCRIPTION_ID')
    ACR_USERNAME = credentials('ACR_USERNAME')  // Store ACR username as a Jenkins credential
    ACR_PASSWORD = credentials('ACR_PASSWORD')
    ACR_REPO = "helloworld"
  }

    stages {
        stage('List Files in Workspace') {
            steps {
                 sh 'ls -R'
            }
         }

        stage('Checkout Code') {
            steps {
                // Replace with your public repository URL
                git url: 'https://github.com/ofenloch/hello-world.git', branch: 'main'
                sh 'echo "Workspace contents:"'
                sh 'ls -R /var/lib/jenkins/workspace/acrpipeline/'
            }
        }
        
        stage('Login to Azure') {
            steps {
                // Log in to Azure CLI
                sh """
                az login --service-principal -u $AZURE_CLIENT_ID -p $AZURE_CLIENT_SECRET --tenant $AZURE_TENANT_ID
                az account set --subscription $AZURE_SUBSCRIPTION_ID
                """
            }
        }

        stage('Login to ACR') {
            steps {
                script {
                    // Inline version of the dockerLogin.groovy script
                    def loginToDockerRegistry = { acrName, acrUsername, acrPassword ->
                        def acrLoginServer = "${acrName}.azurecr.io"
                        echo "Logging in to Azure Container Registry: ${acrLoginServer} using ACR credentials."
                        sh "echo ${acrPassword} | docker login ${acrLoginServer} -u ${acrUsername} --password-stdin"
                    }

                    // Call the login function
                    loginToDockerRegistry(env.ACR_NAME, env.ACR_USERNAME, env.ACR_PASSWORD)
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                // Build Docker image
                sh "docker build -t ${env.ACR_LOGIN_SERVER}/${env.ACR_REPO}/${env.DOCKER_IMAGE_NAME}:${env.DOCKER_IMAGE_TAG} ."
            }
        }

        stage('Push Docker Image') {
            steps {
                // Push Docker image to ACR
                sh "docker push ${env.ACR_LOGIN_SERVER}/${env.ACR_REPO}/${env.DOCKER_IMAGE_NAME}:${env.DOCKER_IMAGE_TAG}"
            }
        }
    }

    
}
