pipeline {
    agent none 

    environment {
        DOCKER_CREDENTIALS_ID = 'dockerhub-credentials'
        SPRING_IMAGE_REPO = 'ella00/munggae-be-spring'
        FASTAPI_IMAGE_REPO = 'ella00/munggae-be-fastapi'
    }

    stages {
        stage('Checkout') {
            agent { label 'java-docker' }
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/main']], 
                          extensions: [[$class: 'CleanBeforeCheckout']], 
                          userRemoteConfigs: [[url: 'https://github.com/Kakaotech-10/munggae_be.git']]])
            }
        }

        stage('Parallel Build') {
            parallel {
                stage('Build Spring') {
                    agent { label 'java-docker' }
                    steps {
                        script {
                            sh './gradlew build -x test'
                            sh 'ls build/libs'
                            stash includes: 'build/libs/*.jar', name: 'spring-jar'
                        }
                    }
                }
                // Build FastAPI 단계는 생략
            }
        }
        /*
        stage('Build Docker Images') {
            parallel {
                stage('Build Spring Docker Image') {
                    agent { label 'dind-agent' }
                    steps {
                        unstash 'spring-jar'
                        script {
                            sh 'ls build/libs'
                            sh 'docker build -t $SPRING_IMAGE_REPO:latest -f Dockerfile.spring .'
                        }
                    }
                }
                // Build FastAPI Docker Image 단계는 생략
            }
        }
        */

        stage('Push Docker Images') {
            parallel {
                stage('Push Spring Docker Image') {
                    agent { label 'dind-agent' }
                    steps {
                        unstash 'spring-jar'
                        withCredentials([usernamePassword(credentialsId: DOCKER_CREDENTIALS_ID, usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) 
                        {
                            script {
                                sh 'ls build/libs'
                                sh 'docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD'
                                sh 'docker build -t $SPRING_IMAGE_REPO:latest -f Dockerfile.spring .'
                                sh 'docker push $SPRING_IMAGE_REPO:latest'
                            }
                        }
                    }
                }
                // Push FastAPI Docker Image 단계는 생략
            }
        }
    }
}
