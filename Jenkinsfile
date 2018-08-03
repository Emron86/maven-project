pipeline {
	agent any
	
	tools {
		maven 'localMaven'
	}
	
	/* alternative way of settings environment variables */
	environment {
		VARNAME = 'someValue'
	}
	
	/* set environment variables in Jenkins: Manage Jenkins -> Configure System -> Check 'Environment Variables' */
	parameters {
		string(name: 'tomcat_dev', defaultValue: '18.222.225.114', description: 'Staging Server')
		string(name: 'tomcat_prod', defaultValue: '18.222.221.77', description: 'Production Server')
		string(name: 'HOMEPATH', defaultValue: env.HOMEPATH_DEV, description: 'Homepath (e.g. users/username)')
		string(name: 'HOMEDRIVE', defaultValue: env.HOMEDRIVE_DEV, description: 'Home drive, e.g. C:')
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
		
		stage('Deployments/Analyze') {
			parallel {
				stage('Deploy to staging') {
					steps {
						/* on pc-machine use setx: setx -m homepath_linux "c:/users/username"*/
						/* need to set permissions in virtual machine for ec2-user to get permission to group tomcat and related folders
						sudo usermod -a -G tomcat ec2-user */
						sh "scp -i $homepath_linux/.ssh/tomcat-demo.pem **/target/*.war ec2-user@${params.tomcat_dev}:/var/lib/tomcat7/webapps"
					}
				}
				
				stage('Deploy to production') {
					steps {
						sh "scp -i $homepath_linux/.ssh/tomcat-demo.pem **/target/*.war ec2-user@${params.tomcat_prod}:/var/lib/tomcat7/webapps"
					}
				}
				
				stage('Checkstyle') {
					steps {
						build job 'static analysis'
					}
				}
			}
		}
	}
}