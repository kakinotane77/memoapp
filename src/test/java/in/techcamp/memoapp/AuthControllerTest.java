package in.techcamp.memoapp;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.mybatis.spring.annotation.MapperScan;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@MapperScan("in.techcamp.memoapp") // Mapper のスキャン
public class AuthControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private MemoMapper memoMapper; // 必要な場合 Mock も作成

    @Autowired
    private AuthController authController;

    @Test
    public void testRegisterUser_Success() throws Exception {
        // Mock のセットアップ
        when(userService.userExists("testuser", "test@example.com")).thenReturn(false);

        // テストロジック
        // MockMvcを使用してコントローラーの動作を確認する
    }
}