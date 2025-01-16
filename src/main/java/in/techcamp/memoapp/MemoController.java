package in.techcamp.memoapp;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.web.csrf.CsrfToken;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@AllArgsConstructor
public class MemoController {

    private final MemoRepository memoRepository;

    @ModelAttribute
    public void addCsrfToken(CsrfToken token, Model model) {
        model.addAttribute("_csrf", token);
    }

    @GetMapping("/memoForm")
    public String showMemoForm(@ModelAttribute("memoForm") MemoForm form, Model model) {
        model.addAttribute("_csrf", SecurityContextHolder.getContext().getAuthentication().getDetails());
        if (form.getDisplayMode() == null) {
            form.setDisplayMode("horizontal");
        }
        return "memoForm";
    }

    @PostMapping("/memos")
    public String createMemo(@ModelAttribute MemoForm memoForm, Model model) {
        try {
            System.out.println("MemoForm content: " + memoForm.getContent());
            System.out.println("MemoForm displayMode: " + memoForm.getDisplayMode());

            if (memoForm.getContent() == null || memoForm.getContent().trim().isEmpty()) {
                model.addAttribute("error", "本文を入力してください。");
                return "memoForm";
            }

            LocalDateTime now = LocalDateTime.now();
            memoForm.setCreatedDate(now);
            memoForm.setUpdatedDate(now);

            System.out.println("Inserting memo into database...");
            memoRepository.insert(
                    memoForm.getContent(),
                    memoForm.getCreatedDate(),
                    memoForm.getUpdatedDate(),
                    memoForm.getDisplayMode() != null ? memoForm.getDisplayMode() : "horizontal"
            );
            System.out.println("Memo inserted successfully.");
            return "redirect:/";
        } catch (Exception e) {
            System.out.println("Error during memo creation: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "メモの作成に失敗しました: " + e.getMessage());
            model.addAttribute("memoForm", memoForm);
            return "memoForm";
        }
    }

    @GetMapping("/")
    public String showMemos(Model model) {
        List<MemoEntity> memoList = memoRepository.findAll();

        // ここでデバッグ表示
        System.out.println("=== showMemos() ===");
        if (memoList != null) {
            for (MemoEntity memo : memoList) {
                System.out.println("id=" + memo.getId()
                        + ", content=" + memo.getContent()
                        + ", updatedDate=" + memo.getUpdatedDate());
            }
        } else {
            System.out.println("memoList is null!");
        }

        model.addAttribute("memoList", memoList);
        return "index";
    }

    @GetMapping("/memos/{id}")
    public String memoDetail(@PathVariable long id, Model model) {
        MemoEntity memo = memoRepository.findById(id);

        if (memo == null) {
            return "redirect:/";
        }

        MemoForm memoForm = new MemoForm();
        memoForm.setContent(memo.getContent());
        memoForm.setDisplayMode(memo.getDisplayMode() != null ? memo.getDisplayMode() : "horizontal"); // 表示モードをセット

        model.addAttribute("memoForm", memoForm);
        model.addAttribute("memo", memo);
        return "detail";
    }

    @PostMapping("/memos/{id}/update")
    public String updateMemo(@PathVariable long id, @ModelAttribute MemoForm memoForm, Model model) {
        try {
            MemoEntity existingMemo = memoRepository.findById(id);
            if (existingMemo == null) {
                model.addAttribute("error", "指定されたメモが見つかりません。");
                return "error";
            }

            LocalDateTime now = LocalDateTime.now();
            memoForm.setUpdatedDate(now);

            memoRepository.update(
                    memoForm.getContent(),
                    memoForm.getUpdatedDate(),
                    memoForm.getDisplayMode() != null ? memoForm.getDisplayMode() : "horizontal",
                    id
            );
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", "メモの更新に失敗しました: " + e.getMessage());
            model.addAttribute("memoForm", memoForm);
            return "detail";
        }
    }

    @PostMapping("/memos/{id}/delete")
    public String deleteMemo(@PathVariable long id, Model model) {
        try {
            memoRepository.deleteById(id);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", "メモの削除に失敗しました: " + e.getMessage());
            return "detail";
        }
    }
}