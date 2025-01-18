DROP INDEX IF EXISTS idx_content_fulltext;
DROP INDEX IF EXISTS idx_user_id;
DROP TABLE IF EXISTS memos;
DROP TABLE IF EXISTS users;

-- users テーブル
CREATE TABLE IF NOT EXISTS users (
  id BIGSERIAL PRIMARY KEY, -- AUTO_INCREMENT の代わりに SERIAL 型を使用
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(50) NOT NULL DEFAULT 'USER',
  email VARCHAR(100) NOT NULL UNIQUE,
  created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- memos テーブル
CREATE TABLE IF NOT EXISTS memos (
  id SERIAL PRIMARY KEY, -- AUTO_INCREMENT の代わりに SERIAL 型を使用
  user_id BIGINT NOT NULL,
  content TEXT NOT NULL, -- PostgreSQLでは LONGTEXT は TEXT 型に変換
  display_mode VARCHAR(50) DEFAULT 'horizontal', -- display_mode 列を追加
  created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- memos テーブルのインデックス
CREATE INDEX idx_user_id ON memos(user_id);

-- memos テーブルの FULLTEXT インデックス
CREATE INDEX idx_content_fulltext ON memos USING gin(to_tsvector('english', content));