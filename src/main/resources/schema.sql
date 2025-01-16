-- users テーブル
CREATE TABLE IF NOT EXISTS users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(50) NOT NULL DEFAULT 'USER',
  email VARCHAR(100) NOT NULL UNIQUE,
  created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- memos テーブル
CREATE TABLE IF NOT EXISTS memos (
  id INT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  title VARCHAR(256) NOT NULL,
  content LONGTEXT NOT NULL,
  created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  INDEX idx_user_id (user_id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- memos テーブルの FULLTEXT インデックス
ALTER TABLE memos
ADD FULLTEXT(content);