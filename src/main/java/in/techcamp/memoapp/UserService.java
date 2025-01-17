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
        // 重複チェックは userExists() で行うので、ここでは不要またはメールだけのチェックにする
        if (userExists(userEntity.getUsername(), userEntity.getEmail())) {
            throw new IllegalArgumentException("既に使用されているユーザー名またはメールアドレスです");
        }
        userMapper.insertUser(userEntity);
    }

    // ユーザー名またはメールアドレスで既にユーザーが登録されているかチェックするメソッドを追加
    public boolean userExists(String username, String email) {
        Optional<UserEntity> userByUsername = userMapper.findByUsername(username);
        Optional<UserEntity> userByEmail = userMapper.findByEmail(email);
        return userByUsername.isPresent() || userByEmail.isPresent();
    }
}