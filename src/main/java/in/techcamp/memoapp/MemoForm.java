package in.techcamp.memoapp;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class MemoForm {
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String displayMode; // 縦書き・横書きの表示モード

    // デフォルトコンストラクタで初期値を設定
    public MemoForm() {
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
        this.displayMode = "horizontal"; // 初期値を横書きに設定
    }
}