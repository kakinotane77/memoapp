package in.techcamp.memoapp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor // 引数なしのデフォルトコンストラクタを生成
@Data
public class MemoEntity {
    private long id;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    // 縦書き・横書きの状態を保持するフィールド
    private String displayMode = "horizontal"; // デフォルトは横書き
}