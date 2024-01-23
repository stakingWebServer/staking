pipeline {
    agent any

    environment {
        MODULE_ADMIN = 'module-admin'
        MODULE_API = 'module-api'
        MODULE_CRAWLING = 'module-crawling'
        CURRENT_LOCATION = '/var/lib/jenkins/workspace/staking';
    }
    stages {
        stage('build') {
            parallel {
                stage('module-admin(build)') {
                    when {
                        anyOf{
                            changeset "module-database/**/*"
                            changeset "module-admin/**/*"
                        }
                    }
                    steps {
                        echo '[build start] ${MODULE_ADMIN}'
                        sh './gradlew ${MODULE_ADMIN}:build -x test'
                        echo '[build end] ${MODULE_ADMIN}'
                    }
                }
                stage('module-api(build)') {
                    when {
                        anyOf{
                            changeset "module-database/**/*"
                            changeset "module-api/**/*"
                        }
                    }
                    steps {
                        echo '[build start] ${MODULE_API}'
                        sh './gradlew ${MODULE_API}:build -x test'
                        echo '[build end] ${MODULE_API}'
                    }
                }
                stage('module-crawling(build)') {
                    when {
                        anyOf{
                            changeset "module-database/**/*"
                            changeset "module-crawling/**/*"
                        }
                    }
                    steps {
                        echo '[build start] ${MODULE_CRAWLING}'
                        sh './gradlew ${MODULE_CRAWLING}:build -x test'
                        echo '[build end] ${MODULE_CRAWLING}'
                    }
                }
    }
}
        stage('deploy') {
            parallel {
                stage('module-admin(deploy)') {
                    when {
                        anyOf{
                            changeset "module-database/**/*"
                            changeset "module-admin/**/*"
                        }
                    }
                    steps {
                        echo '[deploy start] ${MODULE_ADMIN}'
                        sh 'pwd'
                        sh "cd ${CURRENT_LOCATION}/module-admin/"
                        sh 'pwd'
                        echo '[deploy end] ${MODULE_ADMIN}'
                    }
                }
                stage('module-api(deploy)') {
                    when {
                        anyOf{
                            changeset "module-database/**/*"
                            changeset "module-api/**/*"
                        }
                    }
                    steps {
                        echo '[deploy start] ${MODULE_API}'

                        echo '[deploy end] ${MODULE_API}'
                    }
                }
                stage('module-crawling(deploy)') {
                    when {
                        anyOf{
                            changeset "module-database/**/*"
                            changeset "module-crawling/**/*"
                        }
                    }
                    steps {
                        echo '[deploy start] ${MODULE_CRAWLING}'

                        echo '[deploy end] ${MODULE_CRAWLING}'
                    }
                }
            }
        }

           }
}
