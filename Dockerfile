# 1. ビルド環境（Maven 3.8.5 + Java 17）
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# 2. 実行環境（Eclipse Temurin 17 を使用 - こちらが現在の標準です）
FROM eclipse-temurin:17-jdk
COPY --from=build /target/*.war app.war
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.war"]
