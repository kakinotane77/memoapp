package in.techcamp.memoapp;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {

    public static void main(String[] args) {
        // BCryptPasswordEncoderインスタンスを作成
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // 暗号化するパスワード
        String rawPassword = "password"; // ここに平文のパスワードを入力

        // パスワードをBCrypt形式に暗号化
        String encodedPassword = encoder.encode(rawPassword);

        // 結果を出力
        System.out.println("Raw Password: " + rawPassword);
        System.out.println("Encoded Password: " + encodedPassword);
    }
}