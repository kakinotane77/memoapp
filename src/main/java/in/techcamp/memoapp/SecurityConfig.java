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
                .csrf() // CSRF保護はデフォルトで有効
                .and()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register", "/css/**", "/js/**").permitAll() // 特定URLを許可
                        .anyRequest().authenticated() // それ以外は認証が必要
                )
                .formLogin(form -> form
                        .loginPage("/login") // カスタムログインページ
                        .permitAll()
                        .defaultSuccessUrl("/") // ログイン成功後のリダイレクト先
                        .failureUrl("/login?error") // ログイン失敗時のリダイレクト先
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // ログアウトURL
                        .logoutSuccessUrl("/login?logout") // ログアウト成功後のリダイレクト先
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}