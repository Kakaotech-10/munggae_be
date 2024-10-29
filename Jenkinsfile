pipeline {
    agent none  // 전체 파이프라인에 대해 기본 에이전트를 지정하지 않음

    environment {
        DOCKER_CREDENTIALS_ID = credentials('dockerhub-credentials')
        SPRING_IMAGE_REPO = 'ella00/munggae-be-spring'
        FASTAPI_IMAGE_REPO = 'ella00/munggae-be-fastapi'
    }

    stages {
        stage('Checkout') {
            agent { label 'java-docker' }  // Checkout 단계에 java-docker 에이전트 사용
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
                        }
                    }
                }
                /*
                stage('Build FastAPI') {
                    steps {
                        script {
                            node('java-docker') {
                                sh 'pip install -r requirements.txt'
                            }
                        }
                    }
                }
                */
            }
        }

        stage('Build Docker Images') {
            parallel {
                stage('Build Spring Docker Image') {
                    agent { label 'dind-agent' }
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
                            node('dind-agent') {
                                sh 'docker build -t $FASTAPI_IMAGE_REPO:latest -f Dockerfile.fastapi .'
                            }
                        }
                    }
                }
                */
            }
        }

        stage('Push Docker Images') {
            parallel {
                stage('Push Spring Docker Image') {
                    agent { label 'dind-agent' }
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
                            node('dind-agent') {
                                // Docker Hub에 로그인하고 FastAPI 이미지 푸시
                                docker.withRegistry('https://index.docker.io/v1/', DOCKER_CREDENTIALS_ID) {
                                    sh 'docker push $FASTAPI_IMAGE_REPO:latest'
                                }
                            }
                        }
                    }
                }
                */
            }
        }
    }
}