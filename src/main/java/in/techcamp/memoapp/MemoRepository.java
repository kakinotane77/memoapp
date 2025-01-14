package in.techcamp.memoapp;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MemoRepository {
    @Insert("insert into memos(title,content,created_date,updated_date) values (#{title},#{content},#{createdDate},#{updatedDate})")
    void insert(String title,String content,java.time.LocalDateTime createdDate,java.time.LocalDateTime updatedDate);

    @Select("select * from memos")
    List<MemoEntity> findAll();

    @Select("select * from memos where id = #{id}")
    MemoEntity findById(long id);

    @Update("UPDATE memos SET title = #{title}, content = #{content}, updated_date = #{updatedDate} WHERE id = #{id}")
    void update(String title, String content, java.time.LocalDateTime updatedDate, long id);

    @Delete("delete from memos where id=#{id}")
    void deleteById(Long id);
}
