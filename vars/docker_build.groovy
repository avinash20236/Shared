def call(String Project,String ImageTag,String dockerhubuser ){
  withCredentials([usernamePassword(
                    credentialsId: 'dockerhubcred',
                    usernameVariable: 'dockerhubuser',
                    passwordVariable: 'dockerhubpass'
                )]) {

                    sh '''
                        echo "$dockerhubpass" | docker login -u "$dockerhubuser" --password-stdin
                        docker image tag ${Project}:latest $dockerhubuser/${Project}:${imageTag}
                        docker push \$dockerhubuser/${Project}:${ImageTag}
                    '''
  }
}
