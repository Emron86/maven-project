pipeline {
	agent any
	
	tools {
		maven 'localMaven'
	}
	
	/*
	environment {
		HOMEPATH = 'Users/emil.vahlstrom'
		HOMEDRIVE = 'C:'
	}*/
	
	parameters {
		string(name: 'tomcat_dev', defaultValue: '18.222.225.114', description: 'Staging Server')
		string(name: 'tomcat_prod', defaultValue: '18.222.221.77', description: 'Production Server')
		string(name: 'HOMEPATH', defaultValue: '', description: 'Homepath (e.g. users/username)')
		string(name: 'HOMEDRIVE', defaultValue: 'C:', description: 'Home drive, e.g. C:')
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
					echo env.devuser
					echo "${env.devuser}"
					echo "${env.HOMEPATH_DEV}"
					echo env.HOMEPATH_DEV
                }
            }
        }
		
		stage('Deployments') {
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
			}
		}
	}
}