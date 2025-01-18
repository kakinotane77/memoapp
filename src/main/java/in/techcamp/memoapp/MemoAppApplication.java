package in.techcamp.memoapp;

import in.techcamp.memoapp.DotenvConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("in.techcamp.memoapp")
public class MemoAppApplication {

	public static void main(String[] args) {
		// 環境変数を取得
		String mysqlUrl = DotenvConfig.get("MYSQL_URL");
		String mysqlUsername = DotenvConfig.get("MYSQL_USERNAME");
		String mysqlPassword = DotenvConfig.get("MYSQL_PASSWORD");

		// 環境変数が設定されていない場合にエラーをスロー
		if (mysqlUrl == null || mysqlUsername == null || mysqlPassword == null) {
			throw new IllegalArgumentException("Required environment variables (MYSQL_URL, MYSQL_USERNAME, MYSQL_PASSWORD) are not set");
		}

		// 環境変数を Spring Boot プロパティに設定
		System.setProperty("MYSQL_URL", mysqlUrl);
		System.setProperty("MYSQL_USERNAME", mysqlUsername);
		System.setProperty("MYSQL_PASSWORD", mysqlPassword);

		// アプリケーション起動
		SpringApplication.run(MemoAppApplication.class, args);
	}
}