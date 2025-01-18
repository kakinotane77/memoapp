package in.techcamp.memoapp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("in.techcamp.memoapp")
public class MemoAppApplication {

	public static void main(String[] args) {
		// 環境変数を取得
		String rawDatabaseUrl = System.getenv("DATABASE_URL");
		String databaseUsername = System.getenv("POSTGRES_USERNAME");
		String databasePassword = System.getenv("POSTGRES_PASSWORD");

		// 環境変数が設定されていない場合にエラーをスロー
		if (rawDatabaseUrl == null || databaseUsername == null || databasePassword == null) {
			throw new IllegalArgumentException(
					"Required environment variables (DATABASE_URL, POSTGRES_USERNAME, POSTGRES_PASSWORD) are not set"
			);
		}

		// DATABASE_URL を解析して JDBC URL を生成
		String jdbcUrl = convertToJdbcUrl(rawDatabaseUrl);

		// Spring Boot のデータソースプロパティに設定
		System.setProperty("spring.datasource.url", jdbcUrl);
		System.setProperty("spring.datasource.username", databaseUsername);
		System.setProperty("spring.datasource.password", databasePassword);

		// アプリケーション起動
		SpringApplication.run(MemoAppApplication.class, args);
	}

	/**
	 * DATABASE_URL を JDBC URL に変換するメソッド
	 * 例:
	 * postgresql://user:password@host:port/database
	 * -> jdbc:postgresql://host:port/database?user=user&password=password
	 */
	private static String convertToJdbcUrl(String rawUrl) {
		try {
			if (!rawUrl.startsWith("postgresql://")) {
				throw new IllegalArgumentException("Invalid DATABASE_URL format. Expected to start with 'postgresql://'.");
			}

			// プロトコル部分を削除
			String strippedUrl = rawUrl.replaceFirst("postgresql://", "");

			// ユーザー情報、ホスト、データベース部分を分割
			String[] userInfoAndHost = strippedUrl.split("@");
			String[] userInfo = userInfoAndHost[0].split(":");
			String[] hostAndDb = userInfoAndHost[1].split("/", 2);

			// ユーザー名とパスワード
			String username = userInfo[0];
			String password = userInfo[1];

			// ホストとデータベース名
			String host = hostAndDb[0];
			String database = hostAndDb[1];

			// JDBC URL を生成
			return String.format("jdbc:postgresql://%s/%s?user=%s&password=%s", host, database, username, password);

		} catch (Exception e) {
			throw new IllegalArgumentException("Failed to parse DATABASE_URL. Ensure it is in the correct format.", e);
		}
	}
}