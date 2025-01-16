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
        // メールアドレスの重複チェックのみを行う
        Optional<UserEntity> existingUserByEmail = userMapper.findByEmail(userEntity.getEmail());
        if (existingUserByEmail.isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + userEntity.getEmail());
        }

        // ユーザー登録処理
        userMapper.insertUser(userEntity);
    }
}