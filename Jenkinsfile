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
                        sh "sudo mv ${CURRENT_LOCATION}/module-admin/build/libs/module-admin-1.0-SNAPSHOT.jar /app/project"
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
                        script {
                        def pid
                        try {
                        echo '[kill port ${MODULE_ADMIN}]'
                        pid = sh(script: "sudo lsof -t -i :9500 -s TCP:LISTEN",returnStdout: true).trim()
                        }catch(Exception e){
                            echo "오류 내용 : ${e.message}"
                            pid = null
                        }
                       if(pid != "" && pid != null){
                        echo '현재 PID : ${pid}'
                        sh "sudo kill -9 ${pid}"
                        }
                        else{
                            echo "not exist port"
                        }
                        echo '[deploy start] ${MODULE_ADMIN}'
                        sh "cd /app/project"
                        sh "pwd"
                        sh "JENKINS_NODE_COOKIE=dontKillMe && sudo nohup java -jar -Dserver.port=9500 -Duser.timezone=Asia/Seoul module-admin-1.0-SNAPSHOT.jar &"
                        echo '[deploy end] ${MODULE_ADMIN}'
                        }
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
