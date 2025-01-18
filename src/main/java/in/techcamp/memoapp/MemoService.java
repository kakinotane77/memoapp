package in.techcamp.memoapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void createMemo(String username, String content, String displayMode) {
        logger.debug("Start createMemo: username={}, content={}, displayMode={}", username, content, displayMode);

        // ユーザーIDを取得
        Long userId = memoMapper.findUserIdByUsername(username);
        logger.debug("Fetched userId: {}", userId);

        if (userId == null) {
            throw new IllegalArgumentException("ユーザーが存在しません: " + username);
        }

        // メモをデータベースに挿入
        try {
            memoMapper.insertMemo(userId, content, displayMode);
            logger.debug("Memo inserted for userId: {}", userId);

            // 挿入後にデータを再取得して確認
            List<MemoEntity> memos = memoMapper.findMemosByUserId(userId);
            logger.debug("Inserted memo check: {}", memos);
        } catch (Exception e) {
            logger.error("Error during memo insertion: {}", e.getMessage(), e);
            throw e; // 必要に応じて例外を再スロー
        }
    }

    /**
     * ユーザー名でメモを取得する
     */
    public List<MemoEntity> getMemosByUsername(String username) {
        logger.debug("Fetching memos for username: {}", username);

        Long userId = memoMapper.findUserIdByUsername(username);
        if (userId == null) {
            throw new IllegalArgumentException("ユーザーが存在しません: " + username);
        }

        List<MemoEntity> memos = memoMapper.findMemosByUserId(userId);
        logger.debug("Fetched memos: {}", memos);

        return memos;
    }

    /**
     * メモIDとユーザー名でメモを取得する
     */
    public MemoEntity getMemoByIdAndUsername(long id, String username) {
        logger.debug("Fetching memo by id: {}, username: {}", id, username);

        Long userId = memoMapper.findUserIdByUsername(username);
        if (userId == null) {
            throw new IllegalArgumentException("ユーザーが存在しません: " + username);
        }

        MemoEntity memo = memoMapper.findMemoByIdAndUserId(id, userId);
        logger.debug("Fetched memo: {}", memo);

        return memo;
    }

    /**
     * メモを更新する
     */
    public void updateMemo(long id, String username, String content, String displayMode) {
        logger.debug("Updating memo: id={}, username={}, content={}, displayMode={}", id, username, content, displayMode);

        Long userId = memoMapper.findUserIdByUsername(username);
        if (userId == null) {
            throw new IllegalArgumentException("ユーザーが存在しません: " + username);
        }

        memoMapper.updateMemo(id, userId, content, displayMode);
        logger.debug("Memo updated for id: {}, userId: {}", id, userId);
    }

    /**
     * メモを削除する
     */
    public void deleteMemo(long id, String username) {
        logger.debug("Deleting memo: id={}, username={}", id, username);

        Long userId = memoMapper.findUserIdByUsername(username);
        if (userId == null) {
            throw new IllegalArgumentException("ユーザーが存在しません: " + username);
        }

        memoMapper.deleteMemo(id, userId);
        logger.debug("Memo deleted for id: {}, userId: {}", id, userId);
    }
}