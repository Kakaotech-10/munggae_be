pipeline {
    agent none 

    environment {
        DOCKER_CREDENTIALS_ID = 'dockerhub-credentials'
        SPRING_IMAGE_REPO = 'ella00/munggae-be-spring'
        FASTAPI_IMAGE_REPO = 'ella00/munggae-be-fastapi'
    }

    stages {
        stage('Checkout Application Repository') {
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

        stage('Build and Push Docker Images') {
            parallel {
                stage('Build and Push Spring Docker Image') {
                    agent { label 'dind-agent' }
                    steps {
                        unstash 'spring-jar'
                        withCredentials([usernamePassword(credentialsId: DOCKER_CREDENTIALS_ID, usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                            script {
                                def imageTag = "${env.BUILD_NUMBER}"  // Jenkins 빌드 번호 사용
                                sh 'ls build/libs'
                                sh "docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD"
                                sh "docker build -t ${SPRING_IMAGE_REPO}:${imageTag} -f Dockerfile.spring ."
                                sh "docker push ${SPRING_IMAGE_REPO}:${imageTag}"
                            }
                        }
                    }
                }
                // FastAPI Docker 이미지 빌드 및 푸시는 생략
            }
        }

        stage('Checkout Manifest Repository') {
            agent { label 'java-docker' }
            steps {
                script {
                    checkout([$class: 'GitSCM', branches: [[name: '*/main']], 
                              extensions: [[$class: 'CleanBeforeCheckout']], 
                              userRemoteConfigs: [[url: 'https://github.com/Kakaotech-10/munggae-manifest.git']]])
                    withCredentials([usernamePassword(credentialsId: 'github-token', usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD')]) {
                    sh """
                    git config --local user.email "als33396dn@gmail.com"
                    git config --local user.name "KimMinWoooo"

                    # Ensure on main branch
                    git checkout -B main origin/main

                    echo "Current directory contents:"
                    ls -la

                    # Navigate to Backend directory if it exists
                    if [ -d "Backend" ]; then
                      cd Backend
                    else
                      echo "Backend directory not found. Listing current directory contents."
                    fi

                    echo "Current directory after navigation:"
                    ls -la
                    
                    # Update image tag in deployment.yaml with the new build number
                    sed -i "s|image: ella00/munggae-be-spring:.*|image: ella00/munggae-be-spring:${env.BUILD_NUMBER}|" Spring-Deployment.yaml
                    
                    # Commit and push the changes
                    git add .
                    git commit -m "Update image tag to ${env.BUILD_NUMBER} for ArgoCD"
                    git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/Kakaotech-10/munggae-manifest.git main
                    """
                    }
                }
            }
        }
    }
}