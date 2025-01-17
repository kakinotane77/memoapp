package in.techcamp.memoapp;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUserExists() {
        // テストデータ
        String username = "testuser";
        String email = "test@example.com";

        // Mockで返す値を作成
        UserEntity mockUser = new UserEntity();
        mockUser.setUsername(username);
        mockUser.setEmail(email);

        // モックの設定
        when(userMapper.findByUsernameOrEmail(username, email))
                .thenReturn(Optional.of(mockUser));

        // 実行
        System.out.println("Mocked User: " + mockUser);
        System.out.println("Expecting username: " + username + ", email: " + email);

        boolean result = userService.userExists(username, email);

        // ログ出力
        System.out.println("Result from userService.userExists: " + result);

        // 検証
        assertTrue(result, "Expected userExists to return true.");
        verify(userMapper, times(1)).findByUsernameOrEmail(eq(username), eq(email));
    }
}