package in.techcamp.memoapp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("in.techcamp.memoapp")
public class MemoAppApplication {

	public static void main(String[] args) {
		// 環境変数を Spring Boot プロパティに設定
		System.setProperty("MYSQL_URL", DotenvConfig.get("MYSQL_URL"));
		System.setProperty("MYSQL_USERNAME", DotenvConfig.get("MYSQL_USERNAME"));
		System.setProperty("MYSQL_PASSWORD", DotenvConfig.get("MYSQL_PASSWORD"));

		// アプリケーション起動
		SpringApplication.run(MemoAppApplication.class, args);
	}
}