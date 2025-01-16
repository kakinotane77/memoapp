package in.techcamp.memoapp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    private Long id;
    private String username;
    private String password;
    private String role; // 権限情報を格納するフィールドを追加
    private String email; // emailフィールドを追加
}