package in.techcamp.memoapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserMapperTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByUsernameOrEmail() {
        // テストデータを準備
        String username = "testuser";
        String email = "test@example.com";

        // データ挿入
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword("testpassword");
        userMapper.insertUser(user);

        // ログ追加: データが正しく挿入されたか確認
        System.out.println("User inserted with username: " + username + ", email: " + email);

        // 実行
        Optional<UserEntity> foundUser = userMapper.findByUsernameOrEmail(username, email);

        // 結果のログ
        System.out.println("Found user: " + (foundUser.isPresent() ? foundUser.get() : "No user found"));

        // 結果を検証
        assertTrue(foundUser.isPresent(), "User should be found");
        assertEquals(username, foundUser.get().getUsername());
        assertEquals(email, foundUser.get().getEmail());
    }
}