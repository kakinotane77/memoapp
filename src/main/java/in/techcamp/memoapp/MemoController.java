package in.techcamp.memoapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.web.csrf.CsrfToken;
import in.techcamp.memoapp.CustomUserDetails;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@AllArgsConstructor
public class MemoController {

    private final MemoService memoService;
    private static final Logger logger = LoggerFactory.getLogger(MemoController.class);

    @ModelAttribute
    public void addCsrfToken(CsrfToken csrfToken, Model model) {
        model.addAttribute("_csrf", csrfToken);
    }

    @GetMapping("/memoForm")
    public String showMemoForm(@ModelAttribute("memoForm") MemoForm form, Model model) {
        if (form.getDisplayMode() == null) {
            form.setDisplayMode("horizontal");
        }
        return "memoForm";
    }

    @PostMapping("/memos")
    public String createMemo(@ModelAttribute MemoForm memoForm, Model model) {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (principal instanceof CustomUserDetails) {
                String username = ((CustomUserDetails) principal).getUsername();

                if (memoForm.getContent() == null || memoForm.getContent().trim().isEmpty()) {
                    model.addAttribute("error", "本文を入力してください。");
                    return "memoForm";
                }

                memoService.createMemo(username, memoForm.getContent(), memoForm.getDisplayMode());
                return "redirect:/";
            } else {
                model.addAttribute("error", "認証されたユーザーが見つかりません。");
                return "memoForm";
            }
        } catch (Exception e) {
            model.addAttribute("error", "メモの作成に失敗しました: " + e.getMessage());
            return "memoForm";
        }
    }

    @GetMapping("/")
    public String showMemos(Model model) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        List<MemoEntity> memoList = memoService.getMemosByUsername(username);

        model.addAttribute("memoList", memoList);
        return "index";
    }

    @GetMapping("/memos/{id}")
    public String showMemo(@PathVariable Long id, Model model) {
        try {
            // ユーザー情報を取得
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof CustomUserDetails) {
                String username = ((CustomUserDetails) principal).getUsername();
                // メモを取得
                MemoEntity memo = memoService.getMemoByIdAndUsername(id, username);

                if (memo == null) {
                    model.addAttribute("error", "メモが見つかりません。");
                    return "error";  // メモが見つからない場合はエラー表示
                }

                // メモを表示するためのフォームを準備
                MemoForm memoForm = new MemoForm();
                memoForm.setContent(memo.getContent());
                memoForm.setDisplayMode(memo.getDisplayMode());
                model.addAttribute("memoForm", memoForm);  // memoFormをビューに渡す

                // メモ情報をビューに渡す
                model.addAttribute("memo", memo);
                return "detail"; // 修正: detailテンプレートを使用
            } else {
                return "error";
            }
        } catch (Exception e) {
            return "error";
        }
    }

    @PostMapping("/memos/{id}/update")
    public String updateMemo(@PathVariable long id, @ModelAttribute MemoForm memoForm, Model model) {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof CustomUserDetails) {
                String username = ((CustomUserDetails) principal).getUsername();

                if (memoForm.getContent() == null || memoForm.getContent().trim().isEmpty()) {
                    model.addAttribute("error", "本文を入力してください。");
                    return "detail";
                }

                memoService.updateMemo(id, username, memoForm.getContent(), memoForm.getDisplayMode());
                return "redirect:/";
            } else {
                model.addAttribute("error", "認証情報が不正です。");
                return "error";
            }
        } catch (Exception e) {
            model.addAttribute("error", "メモの更新に失敗しました: " + e.getMessage());
            return "detail";
        }
    }

    @PostMapping("/memos/{id}/delete")
    public String deleteMemo(@PathVariable long id, Model model) {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof CustomUserDetails) {
                String username = ((CustomUserDetails) principal).getUsername();

                memoService.deleteMemo(id, username);
                return "redirect:/";
            } else {
                model.addAttribute("error", "認証情報が不正です。");
                return "error";
            }
        } catch (Exception e) {
            model.addAttribute("error", "メモの削除に失敗しました: " + e.getMessage());
            return "detail";
        }
    }

    @PostMapping("/saveDisplayMode")
    public ResponseEntity<String> saveDisplayMode(@RequestBody DisplayModeRequest request) {
        // リクエストボディからデータを取得
        String content = request.getContent();
        String displayMode = request.getDisplayMode();

        // デバッグ用ログ（必要に応じて削除）
        System.out.println("Content: " + content);
        System.out.println("DisplayMode: " + displayMode);

        // ここでビジネスロジックを実行またはデータを保存
        // データベースに保存する処理を実装

        return ResponseEntity.ok("Display mode saved successfully");
    }

    public static class DisplayModeRequest {
        private String content;
        private String displayMode;

        // Getters and Setters
        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDisplayMode() {
            return displayMode;
        }

        public void setDisplayMode(String displayMode) {
            this.displayMode = displayMode;
        }
    }
}