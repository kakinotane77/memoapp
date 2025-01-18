# ベースイメージとして Java 21 を使用
FROM openjdk:21-jdk-slim

# 作業ディレクトリを設定
WORKDIR /app

# ビルドしたJARファイルをコンテナにコピー
COPY ./build/libs/memoapp.jar /app/app.jar

# アプリケーションを起動
ENTRYPOINT ["java", "-jar", "app.jar"]