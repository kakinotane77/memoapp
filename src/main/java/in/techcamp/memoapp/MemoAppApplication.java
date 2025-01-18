package in.techcamp.memoapp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URI;
import java.net.URISyntaxException;

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

		try {
			// DATABASE_URL を解析して JDBC URL を生成
			URI dbUri = new URI(rawDatabaseUrl);

			// ユーザー名とパスワードを取得
			String username = dbUri.getUserInfo().split(":")[0];
			String password = dbUri.getUserInfo().split(":")[1];

			// JDBC URL を生成
			String jdbcUrl = String.format("jdbc:postgresql://%s%s",
					dbUri.getHost(),
					dbUri.getPath());

			// ポートが指定されている場合は追加
			if (dbUri.getPort() != -1) {
				jdbcUrl = String.format("jdbc:postgresql://%s:%d%s",
						dbUri.getHost(),
						dbUri.getPort(),
						dbUri.getPath());
			}

			// Spring Boot のデータソースプロパティに設定
			System.setProperty("spring.datasource.url", jdbcUrl);
			System.setProperty("spring.datasource.username", username);
			System.setProperty("spring.datasource.password", password);

		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("Invalid DATABASE_URL format", e);
		}

		// アプリケーション起動
		SpringApplication.run(MemoAppApplication.class, args);
	}
}