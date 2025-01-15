import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                // Basic認証を有効化
                .httpBasic(basic -> basic
                        .realmName("MemoApp") // 任意のRealm名を設定
                )
                // formLogin を無効化
                .formLogin(form -> form
                        .disable()
                )
                .logout(logout -> logout
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // 平文のパスワード "1234" を BCryptPasswordEncoder でハッシュ化
        String encodedPassword = passwordEncoder().encode("1234");

        UserDetails user = User.withUsername("admin") // ユーザー名を "admin" に変更
                .password(encodedPassword) // ハッシュ化されたパスワードを設定
                .roles("ADMIN") // ロールを "ADMIN" に変更 (必要に応じて)
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}