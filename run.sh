#!/bin/bash

# 设置 AWS 凭证环境变量
# export AWS_ACCESS_KEY_ID=""
# export AWS_SECRET_ACCESS_KEY=""
# export AWS_REGION="us-east-1"

# 清理并编译项目
echo "Cleaning and compiling project..."
mvn clean compile

# 运行程序
echo "Running S3TablesDemo..."
mvn exec:java -Dexec.mainClass="com.example.S3TablesDemo" \
    -Dexec.cleanupDaemonThreads=false \
    -DargLine="--add-exports java.base/sun.nio.ch=ALL-UNNAMED \
               --add-exports java.base/sun.security.action=ALL-UNNAMED \
               --add-opens java.base/java.nio=ALL-UNNAMED \
               --add-opens java.base/java.lang=ALL-UNNAMED \
               --add-opens java.base/java.util=ALL-UNNAMED" 
