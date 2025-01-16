package in.techcamp.memoapp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("in.techcamp.memoapp") // Mapperのパッケージを指定
public class MemoAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MemoAppApplication.class, args);
	}
}