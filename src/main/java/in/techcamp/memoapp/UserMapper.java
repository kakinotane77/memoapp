package in.techcamp.memoapp;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Optional;

@Mapper
public interface UserMapper {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    void insertUser(UserEntity user);

    Optional<UserEntity> findByUsernameOrEmail(@Param("username") String username, @Param("email") String email);
}