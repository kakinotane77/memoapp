import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRFを無効化
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/login", "/css/**", "/js/**").permitAll() // 認証不要のパス
                        .anyRequest().authenticated() // その他は認証必須
                )
                .formLogin(form -> form
                        .loginPage("/login") // カスタムログインページ
                        .permitAll() // 誰でもアクセス可能
                        .defaultSuccessUrl("/") // ログイン成功時のリダイレクト先
                        .failureUrl("/login?error") // ログイン失敗時のリダイレクト先
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // ログアウトのURL
                        .logoutSuccessUrl("/login?logout") // ログアウト成功時のリダイレクト先
                        .permitAll() // 誰でもアクセス可能
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}