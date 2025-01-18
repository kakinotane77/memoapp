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

		// 環境変数が設定されていない場合にエラーをスロー
		if (rawDatabaseUrl == null) {
			throw new IllegalArgumentException("Required environment variable (DATABASE_URL) is not set");
		}

		// DATABASE_URL を解析して JDBC URL を生成
		String jdbcUrl = convertToJdbcUrl(rawDatabaseUrl);

		// Spring Boot のデータソースプロパティに設定
		System.setProperty("spring.datasource.url", jdbcUrl);

		// アプリケーション起動
		SpringApplication.run(MemoAppApplication.class, args);
	}

	/**
	 * DATABASE_URL を JDBC URL に変換するメソッド
	 * DATABASE_URL の例:
	 * postgresql://username:password@host:port/database
	 * 変換後:
	 * jdbc:postgresql://host:port/database
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
			String[] hostAndDb = userInfoAndHost[1].split("/", 2);

			// ホストとデータベース名
			String host = hostAndDb[0];
			String database = hostAndDb[1];

			// JDBC URL を生成
			return String.format("jdbc:postgresql://%s/%s", host, database);

		} catch (Exception e) {
			throw new IllegalArgumentException("Failed to parse DATABASE_URL. Ensure it is in the correct format.", e);
		}
	}
}