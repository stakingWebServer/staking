pipeline {
    agent any

    environment {
        MODULE_ADMIN = 'module-admin'
        MODULE_API = 'module-api'
        MODULE_CRAWLING = 'module-crawling'
    }
    stages {
        stage('build') {
            parallel {
                stage('build-module-admin') {
                    when {
                        anyOf{
                            changeset "module-database/**/*"
                            changeset "module-admin/**/*"
                        }
                    }
                    steps {
                        echo 'building module-admin'
                    }
                }
            }
        }
    }
}