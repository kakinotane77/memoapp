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
			// 環境変数から DATABASE_URL を取得
			String databaseUrl = System.getenv("DATABASE_URL");

			if (databaseUrl == null) {
				throw new IllegalArgumentException("Required environment variable DATABASE_URL is not set");
			}

			// URI オブジェクトとして解析
			URI dbUri = new URI(databaseUrl);

			// JDBC URL の作成
			String jdbcUrl = String.format("jdbc:postgresql://%s:%d%s",
					dbUri.getHost(),
					dbUri.getPort(),
					dbUri.getPath()
			);

			// ユーザー名とパスワードを分解
			String[] userInfo = dbUri.getUserInfo().split(":");
			String username = userInfo[0];
			String password = userInfo[1];

			// Spring DataSource 設定
			System.setProperty("spring.datasource.url", jdbcUrl);
			System.setProperty("spring.datasource.username", username);
			System.setProperty("spring.datasource.password", password);

			// ログ出力（デバッグ用）
			System.out.println("JDBC URL: " + jdbcUrl);

			// アプリケーション起動
			SpringApplication.run(MemoAppApplication.class, args);

		} catch (URISyntaxException e) {
			throw new RuntimeException("Invalid DATABASE_URL format", e);
		}
	}
}