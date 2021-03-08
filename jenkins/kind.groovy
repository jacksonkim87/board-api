import groovy.transform.Field

/////////////프로젝트 설정/////////////// 
@Field def GITOPS_NAME = "gitops/board-api"
@Field def PROJECT_NAME = "board-api"

// project git repository
def gitUrl = "https://github.com/jacksonkim87/board-api.git"
def envBranch = "main"

// Container Image Repository
def containerRegistry = "hub.docker.com/repository/docker/jacksonkim/board-api"

// GitOps git Repository
def gitOpsUrl = "github.com/jacksonkim87/gitops.git"
def opsBranch = "main"

//Argocd log 서버명(context)
def argocdContext = "devops-eks-cluster"

/////////////////////////////

pipeline {
     environment {        
         PATH = "$PATH:/usr/local/bin/"  //maven, skaffold, argocd,jq path
       }
    agent any   
    
    stages {   
        stage('Build') {           
            steps {
                checkout scm: [
                    $class: 'GitSCM', 
                    userRemoteConfigs: [[url: "${gitUrl}", credentialsId: 'github-repo' ]], 
                     branches: [[name: 'refs/tags/${TAG}']]],
                     poll: false

                script{                                     
                     docker.withRegistry("https://${containerRegistry}",""){
                        sh "VER=${TAG} skaffold build -f skaffold-cloud.yaml -p hub"
                     }
                }
            }
        } 
        stage('workspace clear'){
            steps {
                cleanWs()
            }
        }
        stage('GitOps Build') {   
            steps{
                print "======board-api-rollout.yaml tag update====="
                git branch: "${opsBranch}", url: "https://${gitOpsUrl}", credentialsId: "github-repo"     
                               
                script{
                    def cmd = " awk '/newTag: /{print  \$2}' ./${PROJECT_NAME}/kustomization.yaml"
                    def originStr =  executeCmdReturn(cmd)
                    if(!originStr){
                        currentBuild.result = 'FAILURE'
                        return
                    }   

                    def originTag= originStr.replaceAll('"','')
                    sh("sed -i.bak 's/${originTag}/${TAG}/g' ./${PROJECT_NAME}/kustomization.yaml")
                    sh("rm ./${PROJECT_NAME}/kustomization.yaml.bak")
                    

                    sh("git add . && git commit -m 'trigger generated tag : ${TAG}  ' ")

                    withCredentials([usernamePassword(credentialsId: 'github-repo', usernameVariable: 'username', passwordVariable: 'password')]){                    
                        sh("git push https://$username:$password@${gitOpsUrl}  ${opsBranch} ")
                    }
                    
                 } 
                
                print "git push finished !!!"
            }

        } 
        stage('argocd sync') {           
            steps { 
                script{
                    sh("argocd context ${argocdContext} ")

                    sh("argocd app sync ${PROJECT_NAME} " )
                }
            }
        }      

        
    }
}



def  executeCmdReturn( cmd){
  return sh(returnStdout: true, script: cmd).trim()
}

def replaceText(str){
    return str.replaceAll('"','')
}