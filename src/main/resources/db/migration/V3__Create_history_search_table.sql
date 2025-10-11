create table history_search(
    id CHAR(36) PRIMARY KEY,          -- UUID lưu dạng chuỗi
    content VARCHAR(255) UNIQUE NOT NULL,
    count INT DEFAULT 1 NOT NULL,
    last_searched_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL
);