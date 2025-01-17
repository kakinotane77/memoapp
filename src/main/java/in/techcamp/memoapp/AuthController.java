package in.techcamp.memoapp;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/register")
    public String showRegisterPage(
            @RequestParam(required = false) String error,
            Model model
    ) {
        model.addAttribute("isLogin", false);
        model.addAttribute("pageTitle", "Register");
        if (error != null) {
            model.addAttribute("errorMessage", error);
        }
        return "auth";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        try {
            // バリデーション: パスワードのチェック
            if (!password.matches("^(?=.*[a-zA-Z])(?=.*\\d).{8,}$")) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "パスワードは8文字以上で<br>英数字を含む必要があります。");
                return "redirect:/register";
            }

            // ユーザーの重複確認
            if (userService.userExists(username, email)) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "既に使用されているユーザー名<br>またはメールアドレスです。");
                return "redirect:/register";
            }

            // パスワードをハッシュ化してユーザー登録
            String hashedPassword = passwordEncoder.encode(password);
            UserEntity userEntity = new UserEntity(username, hashedPassword, email, "USER");
            userService.registerUser(userEntity);

            // 自動ログイン
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(username, password);
            Authentication auth = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(auth);

            // セッションIDの再生成
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate(); // 古いセッションを無効化
            }
            HttpSession newSession = request.getSession(true); // 新しいセッションを生成
            newSession.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace(); // エラー詳細をログに出力
            redirectAttributes.addFlashAttribute("errorMessage", "登録中にエラーが発生しました。");
            return "redirect:/register";
        }
    }

    @GetMapping("/login")
    public String showLoginPage(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String logout,
            Model model
    ) {
        model.addAttribute("isLogin", true);
        model.addAttribute("pageTitle", "Login");

        // エラーがある場合、カスタムエラーメッセージを設定
        if (error != null) {
            model.addAttribute("errorMessage", "ユーザー名またはパスワードが間違っています。");
        }

        // ログアウトメッセージ
        if (logout != null) {
            model.addAttribute("successMessage", "正常にログアウトしました。");
        }

        return "auth";
    }
}