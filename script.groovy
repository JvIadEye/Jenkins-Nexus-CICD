def buildApp(){
    echo "Building Application ..."
}


def pushImage(){
    echo 'Building Image ...'
    sh "docker buildx build -t 192.168.1.231:9006/dockerhosted-repo:${BUILD_NUMBER} ."

    echo 'Pushing image to docker hosted rerpository on Nexus'

    withCredentials([usernamePassword(credentialsId: 'nexus', passwordVariable: 'PSW', usernameVariable: 'USER')]){
        sh "echo ${PSW} | docker login -u ${USER} --password-stdin 192.168.1.231:9006"
        sh "docker push 192.168.1.231:9006/dockerhosted-repo:${BUILD_NUMBER}"
    }
}

def testApp(){
    echo 'Testing Application ...'
}

def deployApp(){
    echo 'Deploying Application ...'
}

def commitChanges(){
        withCredentials([usernamePassword(credentialsId: 'github', passwordVariable: 'PSW', usernameVariable: 'USER')]) {
        sh 'git config --global user.name "jenkins"'
        sh 'git config --global user.email "jenkins@gmail.com"'
        sh "git remote set-url origin https://${USER}:${PSW}@github.com/JvIadEye/nexus-CI-pipeline-for-portfolio.git"

        //sh '''
        //    #!/bin/bash
        //    sed -i 's/Version:.*/Version: '"${BUILD_NUMBER}"'/g' index.html
        //'''
        
        //sh "git add ."
        //sh 'git commit -m "updated version"'
        // sh "git push origin HEAD:main"
  }
  echo 'Changes committed by jenkins'
}

return this
