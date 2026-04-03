# 1. 土台（OS）を決める
FROM maven:3.8.4-openjdk-17 AS build
# 2. Eclipseで作ったプログラムを全部コピーする
COPY . .
# 3. Javaの実行ファイル（warファイル）を組み立てる
RUN mvn clean package -DskipTests

# 4. 実行用の軽い部屋に移動する
FROM openjdk:17-jdk-slim
# 5. 組み立てたファイルだけを持ってくる
COPY --from=build /target/*.war app.war
# 6. 8080番の窓口を開ける
EXPOSE 8080
# 7. アプリを起動する！
ENTRYPOINT ["java", "-jar", "/app.war"]
