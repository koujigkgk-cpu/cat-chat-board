# 1. ビルド環境（Java 21対応のMavenを使用）
FROM maven:3.9.6-eclipse-temurin-21 AS build
COPY . .
RUN mvn clean package -DskipTests

# 2. 実行環境（Java 21）
FROM eclipse-temurin:21-jdk
COPY --from=build /target/*.war app.war
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.war"]
