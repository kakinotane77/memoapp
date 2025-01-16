package in.techcamp.memoapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MemoService {

    private final MemoMapper memoMapper;

    public MemoService(MemoMapper memoMapper) {
        this.memoMapper = memoMapper;
    }
    private static final Logger logger = LoggerFactory.getLogger(MemoService.class);

    /**
     * メモを作成する
     */
    public void createMemo(String username, String content, String displayMode) {
        logger.debug("Start createMemo: username={}, content={}, displayMode={}", username, content, displayMode);

        // ユーザーIDを取得
        Long userId = memoMapper.findUserIdByUsername(username);
        logger.debug("Fetched userId: {}", userId);

        if (userId == null) {
            throw new IllegalArgumentException("ユーザーが存在しません: " + username);
        }

        // メモをデータベースに挿入
        memoMapper.insertMemo(userId, content, displayMode);
        logger.debug("Memo inserted for userId: {}", userId);
    }

    /**
     * ユーザー名でメモを取得する
     */
    public List<MemoEntity> getMemosByUsername(String username) {
        Long userId = memoMapper.findUserIdByUsername(username);
        if (userId == null) {
            throw new IllegalArgumentException("ユーザーが存在しません: " + username);
        }
        return memoMapper.findMemosByUserId(userId);
    }

    /**
     * メモIDとユーザー名でメモを取得する
     */
    public MemoEntity getMemoByIdAndUsername(long id, String username) {
        Long userId = memoMapper.findUserIdByUsername(username);
        if (userId == null) {
            throw new IllegalArgumentException("ユーザーが存在しません: " + username);
        }
        return memoMapper.findMemoByIdAndUserId(id, userId);
    }

    /**
     * メモを更新する
     */
    public void updateMemo(long id, String username, String content, String displayMode) {
        Long userId = memoMapper.findUserIdByUsername(username);
        if (userId == null) {
            throw new IllegalArgumentException("ユーザーが存在しません: " + username);
        }
        memoMapper.updateMemo(id, userId, content, displayMode);
    }

    /**
     * メモを削除する
     */
    public void deleteMemo(long id, String username) {
        Long userId = memoMapper.findUserIdByUsername(username);
        if (userId == null) {
            throw new IllegalArgumentException("ユーザーが存在しません: " + username);
        }
        memoMapper.deleteMemo(id, userId);
    }
}