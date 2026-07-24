def call(){
  pipeline {
    agent {
        label "Vinod"
    }

    stages {

        stage("code") {
            steps {
                echo "this is for coding"
                git url: "https://github.com/avinash20236/main-project-1", branch: "main"
                echo "cloning is successful"
            }
        }

        stage("Check User") {
            steps {
                sh 'whoami'
                sh 'id'
                sh 'groups'
                sh 'docker ps'
            }
        }

        stage("build") {
            steps {
                echo "this is for building code"
                sh 'docker build -t notes-app:latest .'
            }
        }

        stage("Push to Dockerhub") {
            steps {
                echo "this is pushing image to dockerhub"

                withCredentials([usernamePassword(
                    credentialsId: 'dockerhubcred',
                    usernameVariable: 'dockerhubuser',
                    passwordVariable: 'dockerhubpass'
                )]) {

                    sh '''
                        echo "$dockerhubpass" | docker login -u "$dockerhubuser" --password-stdin
                        docker image tag notes-app:latest $dockerhubuser/notes-app:latest
                        docker push $dockerhubuser/notes-app:latest
                    '''
                }
            }
        }

stage("Deploy") {
    steps {
        sh '''
            docker-compose down || true
            docker stop notes-app || true
            docker rm notes-app || true
            docker-compose up -d
        '''
    }
}
    }
}

}
