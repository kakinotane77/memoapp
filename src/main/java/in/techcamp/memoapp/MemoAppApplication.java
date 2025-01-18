package in.techcamp.memoapp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("in.techcamp.memoapp")
public class MemoAppApplication {

	public static void main(String[] args) {
		// PostgreSQL 用の環境変数を取得
		String databaseUrl = System.getenv("DATABASE_URL");
		String databaseUsername = System.getenv("POSTGRES_USERNAME");
		String databasePassword = System.getenv("POSTGRES_PASSWORD");

		// 環境変数が設定されていない場合にエラーをスロー
		if (databaseUrl == null || databaseUsername == null || databasePassword == null) {
			throw new IllegalArgumentException(
					"Required environment variables (DATABASE_URL, POSTGRES_USERNAME, POSTGRES_PASSWORD) are not set"
			);
		}

		// Spring Boot のデータソースプロパティに設定
		System.setProperty("spring.datasource.url", databaseUrl);
		System.setProperty("spring.datasource.username", databaseUsername);
		System.setProperty("spring.datasource.password", databasePassword);

		// アプリケーション起動
		SpringApplication.run(MemoAppApplication.class, args);
	}
}