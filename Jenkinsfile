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

        stage('NEXUS IQ SCANNING'){
            steps{
                script{
                    nexusPolicyEvaluation advancedProperties: '', enableDebugLogging: false, failBuildOnNetworkError: false, failBuildOnScanningErrors: false, iqApplication: selectedApplication('sandbox-application'), iqInstanceId: 'Netpoleon', iqOrganization: 'e3ef0e9231164a11b2675196a7e8ec80', iqStage: 'build', jobCredentialsId: '', reachability: [failOnError: false, java: [options: [], properties: [], tool: ''], javaAnalysis: [algorithm: 'RTA_PLUS', enable: false, entrypointStrategy: 'ACCESSIBLE_CONCRETE', force: false], jsAnalysis: [enable: false, force: false, sourceFiles: [[pattern: '']]]], unstableBuildOnScanningWarnings: false                }
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
                    
                    withCredentials([usernamePassword(credentialsId: 'Aqua', passwordVariable: 'PSW', usernameVariable: 'USER')]){
                    sh '''
                        docker login https://registry.aquasec.com -u huy.tran@netpoleons.com -p Admin@123
                        docker pull registry.aquasec.com/scanner:2022.4
                        docker pull 172.16.16.74:9006/dockerhosted-repo:${BUILD_NUMBER}
                        docker logout

                        docker run -e BUILD_JOB_NAME=${JOB_NAME} -e BUILD_URL=${BUILD_URL} -e BUILD_NUMBER=${BUILD_NUMBER} --rm -v /var/run/docker.sock:/var/run/docker.sock registry.aquasec.com/scanner:2022.4 scan --host https://172.16.16.90:443 --local 172.16.16.74:9006/dockerhosted-repo:${BUILD_NUMBER} --checkonly --no-verify --user ${USER} --password ${PSW} --layer-vulnerabilities                    
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
