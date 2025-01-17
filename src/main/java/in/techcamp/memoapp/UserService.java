package in.techcamp.memoapp;

import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {
    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public void registerUser(UserEntity userEntity) {
        // 重複チェック
        if (userExists(userEntity.getUsername(), userEntity.getEmail())) {
            throw new IllegalArgumentException("既に使用されているユーザー名またはメールアドレスです");
        }
        userMapper.insertUser(userEntity);
    }

    /**
     * ユーザー名またはメールアドレスで既にユーザーが登録されているかチェックする
     * @param username ユーザー名
     * @param email メールアドレス
     * @return 登録されていれば true, それ以外は false
     */
    public boolean userExists(String username, String email) {
        // findByUsernameOrEmail を使用してクエリを1回に統合
        Optional<UserEntity> existingUser = userMapper.findByUsernameOrEmail(username, email);
        return existingUser.isPresent();
    }
}