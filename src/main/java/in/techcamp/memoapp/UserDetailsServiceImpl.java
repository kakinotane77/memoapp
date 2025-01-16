package in.techcamp.memoapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userMapper.findByUsername(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // role が null の場合にデフォルト値を設定
        String role = userEntity.getRole() != null ? userEntity.getRole() : "USER";

        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .roles(role) // デフォルト値を設定済み
                .build();
    }
}