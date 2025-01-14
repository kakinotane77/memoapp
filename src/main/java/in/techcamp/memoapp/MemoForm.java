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

    // コンストラクタでデフォルト値を設定
    public MemoForm() {
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }
}