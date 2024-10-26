pipeline {
    agent {
        label 'java-docker'
    }

    environment {
        DOCKER_CREDENTIALS_ID = credentials('dockerhub-credentials')
        SPRING_IMAGE_REPO = 'ella00/munggae-be-spring'
        FASTAPI_IMAGE_REPO = 'ella00/munggae-be-fastapi'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Parallel Build') {
            parallel {
                stage('Build Spring') {
                    steps {
                        script {
                            sh 'cd spring-project && ./gradlew clean build -x test'
                        }
                    }
                }
                /*
                stage('Build FastAPI') {
                    steps {
                        script {
                            sh 'pip install -r requirements.txt'
                        }
                    }
                }
                */
            }
        }

        stage('Build Docker Images') {
            parallel {
                stage('Build Spring Docker Image') {
                    steps {
                        script {
                            sh 'docker build -t $SPRING_IMAGE_REPO:latest -f Dockerfile.spring .'
                        }
                    }
                }
                /*
                stage('Build FastAPI Docker Image') {
                    steps {
                        script {
                            sh 'docker build -t $FASTAPI_IMAGE_REPO:latest -f Dockerfile.fastapi .'
                        }
                    }
                }
                */
            }
        }

        stage('Push Docker Images') {
            parallel {
                stage('Push Spring Docker Image') {
                    steps {
                        script {
                            docker.withRegistry('https://index.docker.io/v1/', DOCKER_CREDENTIALS_ID) {
                                sh 'docker push $SPRING_IMAGE_REPO:latest'
                            }
                        }
                    }
                }
                /*
                stage('Push FastAPI Docker Image') {
                    steps {
                        script {
                            // Docker Hub에 로그인하고 FastAPI 이미지 푸시
                            docker.withRegistry('https://index.docker.io/v1/', DOCKER_CREDENTIALS_ID) {
                                sh 'docker push $FASTAPI_IMAGE_REPO:latest'
                            }
                        }
                    }
                }
                */
            }
        }
    }
}
