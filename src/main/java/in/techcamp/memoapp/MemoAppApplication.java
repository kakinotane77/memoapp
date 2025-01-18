package in.techcamp.memoapp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("in.techcamp.memoapp")
public class MemoAppApplication {

	public static void main(String[] args) {
		// 環境変数を取得
		String databaseUrl = System.getenv("DATABASE_URL");
		String databaseUsername = System.getenv("POSTGRES_USERNAME");
		String databasePassword = System.getenv("POSTGRES_PASSWORD");

		// 環境変数が設定されていない場合にエラーをスロー
		if (databaseUrl == null || databaseUsername == null || databasePassword == null) {
			throw new IllegalArgumentException(
					"Required environment variables (DATABASE_URL, POSTGRES_USERNAME, POSTGRES_PASSWORD) are not set"
			);
		}

		// JDBC URL の形式に変換
		String jdbcUrl = convertToJdbcUrl(databaseUrl);

		// Spring Boot のデータソースプロパティに設定
		System.setProperty("spring.datasource.url", jdbcUrl);
		System.setProperty("spring.datasource.username", databaseUsername);
		System.setProperty("spring.datasource.password", databasePassword);

		// アプリケーション起動
		SpringApplication.run(MemoAppApplication.class, args);
	}

	/**
	 * DATABASE_URL を JDBC URL の形式に変換するメソッド
	 * 例: postgresql://host:port/database -> jdbc:postgresql://host:port/database
	 */
	private static String convertToJdbcUrl(String databaseUrl) {
		if (!databaseUrl.startsWith("postgresql://")) {
			throw new IllegalArgumentException("Invalid DATABASE_URL format. Expected to start with 'postgresql://'.");
		}
		return "jdbc:" + databaseUrl;
	}
}