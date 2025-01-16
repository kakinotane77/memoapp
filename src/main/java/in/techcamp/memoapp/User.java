package in.techcamp.memoapp;

import java.time.LocalDateTime;

public class User {
    private Integer id; // ユーザーID
    private String username; // ユーザー名
    private String password; // パスワード（暗号化後）
    private String role; // ユーザーの役割（例: USER, ADMIN）
    private LocalDateTime createdDate; // 作成日時

    // Getter and Setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}

