# 1. ビルド環境
FROM maven:3.9.6-eclipse-temurin-21 AS build
COPY . .
RUN mvn clean package -DskipTests

# 2. 実行環境（Tomcat 10 を使用）
FROM tomcat:10.1-jdk21
# 以前のビルドで作成されたWARファイルをTomcatの公開ディレクトリにコピー
COPY --from=build /target/*.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
