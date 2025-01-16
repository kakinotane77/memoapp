package in.techcamp.memoapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    public String memoDetail(@PathVariable long id, Model model) {
        String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        MemoEntity memo = memoService.getMemoByIdAndUsername(id, username);

        if (memo == null) {
            return "redirect:/";
        }

        MemoForm memoForm = new MemoForm();
        memoForm.setContent(memo.getContent());
        memoForm.setDisplayMode(memo.getDisplayMode() != null ? memo.getDisplayMode() : "horizontal");

        model.addAttribute("memoForm", memoForm);
        model.addAttribute("memo", memo);
        return "detail";
    }

    @PostMapping("/memos/{id}/update")
    public String updateMemo(@PathVariable long id, @ModelAttribute MemoForm memoForm, Model model) {
        try {
            String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            if (memoForm.getContent() == null || memoForm.getContent().trim().isEmpty()) {
                model.addAttribute("error", "本文を入力してください。");
                return "detail";
            }

            memoService.updateMemo(id, username, memoForm.getContent(), memoForm.getDisplayMode());
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", "メモの更新に失敗しました: " + e.getMessage());
            return "detail";
        }
    }

    @PostMapping("/memos/{id}/delete")
    public String deleteMemo(@PathVariable long id, Model model) {
        try {
            String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            memoService.deleteMemo(id, username);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", "メモの削除に失敗しました: " + e.getMessage());
            return "detail";
        }
    }
}