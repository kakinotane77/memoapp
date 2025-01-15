package in.techcamp.memoapp;

import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface MemoRepository {

    // INSERT: display_mode を追加して保存
    @Insert("INSERT INTO memos (content, created_date, updated_date, display_mode) VALUES (#{content}, #{createdDate}, #{updatedDate}, #{displayMode})")
    void insert(String content, java.time.LocalDateTime createdDate, java.time.LocalDateTime updatedDate, String displayMode);

    // 全件取得: 必要なカラムを明示的に指定
    @Select("SELECT id, content, created_date, updated_date, display_mode FROM memos")
    List<MemoEntity> findAll();

    // ID指定で1件取得
    @Select("SELECT id, content, created_date, updated_date, display_mode FROM memos WHERE id = #{id}")
    MemoEntity findById(long id);

    // UPDATE: content, updated_date, display_mode を更新
    @Update("UPDATE memos SET content = #{content}, updated_date = #{updatedDate}, display_mode = #{displayMode} WHERE id = #{id}")
    void update(String content, java.time.LocalDateTime updatedDate, String displayMode, long id);

    // DELETE: ID指定で削除
    @Delete("DELETE FROM memos WHERE id = #{id}")
    void deleteById(Long id);
}