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
                        sh "sudo mv ${CURRENT_LOCATION}/module-api/build/libs/module-api-1.0-SNAPSHOT.jar /app/project"
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
                        sh "sudo mv ${CURRENT_LOCATION}/module-crawling/build/libs/module-crawling-1.0-SNAPSHOT.jar /app/project"
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
                        }
                        catch(Exception e){
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
                        sh "JENKINS_NODE_COOKIE=dontKillMe && sudo nohup java -jar -Dserver.port=9500 -Duser.timezone=Asia/Seoul /app/project/module-admin-1.0-SNAPSHOT.jar 1>/dev/null 2>&1 &"
                        echo '[deploy end] ${MODULE_ADMIN}'
                        }
                    }
                }
                stage('module-api-01(deploy)') {
                    when {
                        anyOf{
                            changeset "module-database/**/*"
                            changeset "module-api/**/*"
                        }
                    }
                    steps {
                    script{
                        def pid
                        def response
                        def responseAsString
                        def status = true
                        try {
                        echo '[kill port ${MODULE_API}]'
                        pid = sh(script: "sudo lsof -t -i :8080 -s TCP:LISTEN",returnStdout: true).trim()
                        }
                        catch(Exception e){
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
                        echo '[deploy start] ${MODULE_API}'
                        sh "JENKINS_NODE_COOKIE=dontKillMe && sudo nohup java -jar -Dserver.port=8080 -Duser.timezone=Asia/Seoul /app/project/module-api-1.0-SNAPSHOT.jar 1>/dev/null 2>&1 &"
                        while(status) {
                        response = sh(script: "curl -s -o /dev/null -w '%{http_code}' http://s2it.kro.kr:8080/swagger-ui/index.html", returnStatus: true)
                        responseAsString = response.toString().trim()
                        if(responseAsString == "200"){
                        echo "1번 서버 구동 완료"
                        break
                        }
                        echo "1번 서버 구동 대기중..."
                        sleep 5
                        }
                    }
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
                    script{
                        def pid
                        try {
                        echo '[kill port ${MODULE_API}]'
                        pid = sh(script: "sudo lsof -t -i :9000 -s TCP:LISTEN",returnStdout: true).trim()
                        }
                        catch(Exception e){
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
                        echo '[deploy start] ${MODULE_CRAWLING}'
                        sh "JENKINS_NODE_COOKIE=dontKillMe && sudo nohup java -jar -Dserver.port=9000 -Duser.timezone=Asia/Seoul /app/project/module-crawling-1.0-SNAPSHOT.jar 1>/dev/null 2>&1 &"
                        echo '[deploy end] ${MODULE_CRAWLING}'
                    }
                    }
                }
            }
        }
    }
}
