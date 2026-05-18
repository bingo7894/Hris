pipeline {
    agent any

    stages {
        stage('1. Checkout Code') {
            steps {
                // ขั้นตอนดึงโค้ดล่าสุดมาทำงาน
                checkout scm
            }
        }

        stage('2. Build Project') {
            steps {
                // สั่งคอมไพล์โค้ด Spring Boot เพื่อเตรียมสแกน
                sh './mvnw clean compile'
            }
        }

        stage('3. SonarQube Analysis') {
            steps {
                // 'SonarQube' คือชื่อเซิร์ฟเวอร์ที่เราตั้งไว้ในหน้าจอ Manage Jenkins -> System
                withSonarQubeEnv('SonarQube') {
                    sh './mvnw sonar:sonar -Dsonar.projectKey=hris-backend-system'
                }
            }
        }
    }
}