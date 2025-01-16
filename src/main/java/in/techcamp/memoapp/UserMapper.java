package in.techcamp.memoapp;

import org.apache.ibatis.annotations.Mapper;
import in.techcamp.memoapp.UserEntity;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users WHERE username = #{username}")
    UserEntity findByUsername(String username);
}