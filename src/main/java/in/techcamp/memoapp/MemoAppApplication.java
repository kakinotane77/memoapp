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
		try {
			// DATABASE_URL 環境変数を取得
			String databaseUrl = System.getenv("DATABASE_URL");

			// DATABASE_URL が設定されていない場合はエラーをスロー
			if (databaseUrl == null) {
				throw new IllegalArgumentException("Required environment variable DATABASE_URL is not set");
			}

			// DATABASE_URL をパース
			URI dbUri = new URI(databaseUrl);

			// JDBC URL を構築
			String jdbcUrl = String.format("jdbc:postgresql://%s:%d%s", dbUri.getHost(), dbUri.getPort(), dbUri.getPath());

			// ユーザー名とパスワードを取得
			String username = dbUri.getUserInfo().split(":")[0];
			String password = dbUri.getUserInfo().split(":")[1];

			// Spring Boot のデータソースプロパティに設定
			System.setProperty("spring.datasource.url", jdbcUrl);
			System.setProperty("spring.datasource.username", username);
			System.setProperty("spring.datasource.password", password);

			// アプリケーション起動
			SpringApplication.run(MemoAppApplication.class, args);

		} catch (URISyntaxException e) {
			// URI の形式が不正な場合の例外処理
			throw new RuntimeException("Invalid DATABASE_URL format", e);
		}
	}
}