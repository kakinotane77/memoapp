package in.techcamp.memoapp;

import org.springframework.ui.Model;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@AllArgsConstructor
public class MemoController {

    private final MemoRepository memoRepository;

    /**
     * メモフォームの表示
     */
    @GetMapping("/memoForm")
    public String showMemoForm(@ModelAttribute("memoForm") MemoForm form) {
        return "memoForm";
    }

    /**
     * メモの作成
     */
    @PostMapping("/memos")
    public String createMemo(@ModelAttribute MemoForm memoForm, Model model) {
        try {
            if (memoForm.getContent() == null || memoForm.getContent().trim().isEmpty()) {
                model.addAttribute("error", "本文を入力してください。");
                return "memoForm";
            }

            LocalDateTime now = LocalDateTime.now();
            memoForm.setCreatedDate(now);
            memoForm.setUpdatedDate(now);

            memoRepository.insert(memoForm.getContent(), memoForm.getCreatedDate(), memoForm.getUpdatedDate());
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", "メモの作成に失敗しました: " + e.getMessage());
            return "memoForm";
        }
    }

    /**
     * メモ一覧の表示
     */
    @GetMapping("/")
    public String showMemos(Model model) {
        try {
            var memoList = memoRepository.findAll();
            if (memoList == null || memoList.isEmpty()) {
                model.addAttribute("memoList", List.of()); // 空リストを渡す
            } else {
                model.addAttribute("memoList", memoList);
            }
        } catch (Exception e) {
            model.addAttribute("error", "メモの取得に失敗しました: " + e.getMessage());
            model.addAttribute("memoList", List.of()); // エラー時も空リストを渡す
        }
        return "index";
    }

    /**
     * メモ詳細の表示
     */
    @GetMapping("/memos/{id}")
    public String memoDetail(@PathVariable long id, Model model) {
        MemoEntity memo = memoRepository.findById(id);

        if (memo == null) {
            model.addAttribute("error", "指定されたメモが見つかりません。");
            return "error";
        }

        MemoForm memoForm = new MemoForm();
        memoForm.setContent(memo.getContent());

        model.addAttribute("memoForm", memoForm);
        model.addAttribute("memo", memo);
        return "detail";
    }

    /**
     * メモの更新
     */
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

            memoRepository.update(memoForm.getContent(), memoForm.getUpdatedDate(), id);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", "メモの更新に失敗しました: " + e.getMessage());
            return "detail";
        }
    }

    /**
     * メモの削除
     */
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