package in.techcamp.memoapp;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemoMapper {

    Long findUserIdByUsername(String username);

    void insertMemo(Long userId, String content, String displayMode);

    List<MemoEntity> findMemosByUserId(Long userId);

    MemoEntity findMemoByIdAndUserId(long id, Long userId);

    void updateMemo(long id, Long userId, String content, String displayMode);

    void deleteMemo(long id, Long userId);
}