pipeline {
	agent any
	
	tools {
		maven 'localMaven'
	}
	
	parameters {
		string(name: 'tomcat_dev', defaultValue: '18.222.225.114', description: 'Staging Server')
		string(name: 'tomcat_prod', defaultValue: '18.222.221.77', description: 'Production Server')
	}
	
	triggers {
		pollSCM('* * * * *')
	}
	
	stages {
		stage('Build'){
            steps {
                sh 'mvn clean package'
            }
            post {
                success {
                    echo 'Now Archiving...'
                    archiveArtifacts artifacts: '**/target/*.war'
                }
            }
        }
		
		stage('Deployments') {
			parallel {
				stage('Deploy to staging') {
					steps {
						sh "scp -i $homepath/ssh/tomcat-demo.pem **/target/*.war ec2-user@${params.tomcat_dev}:/var/lib/tomcat7/webapps"
					}
				}
				
				stage('Deploy to production') {
					steps {
						sh "scp -i $homepath/ssh/tomcat-demo.pem **/target/*.war ec2-user@${params.tomcat_prod}:/var/lib/tomcat7/webapps"
					}
				}
			}
		}
	}
	
	/* // old build, saved for educational purposes 
	stages {
		stage('Build') {
			steps {
				sh 'mvn clean package'
			}
			post {
				success {
					echo 'Now Archiving...'
					archiveArtifacts artifacts: '**\/target/*.war'
				}
			}
		}
		stage('Deploy to Staging') {
			steps {
				build job: 'deploy-to-staging'
			}
		}
		stage('Deploy to Production'){
			steps {
				timeout(time:5, unit:'DAYS') {
					input message: 'Approve PRODUCTION Deployment?', 
						submitter: 'admin'
				}
				
				build job: 'deploy-to-prod'
			}
			post {
				success {
					echo 'Code deployed to Production.'
				}
				
				failure {
					echo 'Deployment failed.'
				}
			}
		}
	}
	*/
}