 pipeline {
    agent any
    stages {
       stage('Build') {
           steps {
                dir (path: "./") {
                    sh 'mvn clean compile'
                }
           }
       }
       stage('Redeploy') {
           steps {
                dir (path: "./topology/") {
                    echo "current directory is: ${pwd()}"
                    sh 'docker-compose down --volumes'
                    sh 'docker-compose up -d --build'
                }
                dir (path: "./topology/") {
               		// https://github.com/vishnubob/wait-for-it/
               		sh './wait-for-it.sh -h localhost -p 26257 -t 30'
                }
                dir (path: "./service-users-server/") {
	                sh 'mvn install'
    	            sh 'mvn spring-boot:start'
                }
           }
       }

       stage('API tests') {
           steps {
               dir (path: "./topology/"){
               		sh './wait-for-it.sh -h localhost -p 8080 -t 30'
               }
               dir (path: "./service-users-specs/") {
               		sh 'mvn install'
               }
               echo 'Test results are available on Probe Dock: https://trial.probedock.io/avaliasystems/openaffectserver'
           }
       }

       stage('Undeploy') {
       		steps {
				dir (path: "./service-users-server/") {
                	sh 'mvn install spring-boot:stop'
                }
       			dir (path: "./topology/") {
       				sh 'docker-compose down --volumes'
       			}
       		}
       }

       stage('Validation') {
           steps {
               echo 'Test results are available on Probe Dock: https://trial.probedock.io/avaliasystems/openaffectserver'
           }
       }   
    }
}