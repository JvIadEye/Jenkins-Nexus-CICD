def gv_script
pipeline{
    agent any
    stages{
        stage('Init'){
            steps{
                script{
                    gv_script = load "script.groovy"
                }
            }
        }
        
        stage('Build'){
            steps{
                script{
                    gv_script.buildApp()
                }
            }
        }

        stage('Test'){
            steps{
                script{
                    gv_script.testApp()
                }
            }
        }
        
        stage('Push image'){
            steps{
                script{
                    gv_script.pushImage()
                }
            }
        }

        stage('Deploy'){
            steps{
                script{
                    gv_script.deployApp()
                }
            }
        }

        stage('AQUA CICD scanning'){
            steps{
                script{
                    //withCredentials([usernamePassword(credentialsId: 'nexus', passwordVariable: 'PSW', usernameVariable: 'USER')]){
                    //sh '''
                    //    docker login https://registry.aquasec.com -u ${USER} -p ${PSW}
                    //    docker pull 192.168.1.231:9006/dockerhosted-repo:4
                    //    docker logout
                    //'''
                    //}
                    
                    withCredentials([usernamePassword(credentialsId: 'aqua', passwordVariable: 'PSW', usernameVariable: 'USER')]){
                    sh '''
                        docker login https://registry.aquasec.com -u ${USER} -p ${PSW}
                        docker pull registry.aquasec.com/scanner:2022.4.404
                        docker pull 192.168.1.231:9006/dockerhosted-repo:4
                        docker logout

                        docker run -e BUILD_JOB_NAME=${BUILD_JOB_NAME} -e BUILD_URL=${BUILD_URL} -e BUILD_NUMBER=${BUILD_NUMBER} --rm -v /var/run/docker.sock:/var/run/docker.sock registry.aquasec.com/scanner:2022.4.404 scan --host https://172.16.99.201:30903 --local 192.168.1.231:9006/dockerhosted-repo:27 --checkonly --no-verify --user ${USER} --password ${PSW} --layer-vulnerabilities                    
                    '''
                    }
                }
            }
        }
    }
    post{
        success{
            sh 'echo "Pipeline created successfully!"'
        }
    }
}
