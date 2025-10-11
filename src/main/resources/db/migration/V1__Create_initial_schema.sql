-- Tạo database
-- CREATE DATABASE bigfood;
-- USE bigfood;

-- Bảng User
CREATE TABLE users (
    id CHAR(36) PRIMARY KEY,          -- UUID lưu dạng chuỗi
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20)
);

-- Bảng Role
CREATE TABLE roles (
    name VARCHAR(50) PRIMARY KEY,
    description VARCHAR(255)
);

-- Bảng Permission
CREATE TABLE permissions (
    name VARCHAR(50) PRIMARY KEY,
    description VARCHAR(255)
);

-- Bảng InvalidatedToken
CREATE TABLE invalidated_tokens (
    id VARCHAR(255) PRIMARY KEY,
    expiry_time DATETIME NOT NULL
);

-- Bảng trung gian User ↔ Role
CREATE TABLE user_roles (
    user_id CHAR(36),
    role_name VARCHAR(50),
    PRIMARY KEY (user_id, role_name),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_name) REFERENCES roles(name) ON DELETE CASCADE
);

-- Bảng trung gian Role ↔ Permission
CREATE TABLE role_permissions (
    role_name VARCHAR(50),
    permission_name VARCHAR(50),
    PRIMARY KEY (role_name, permission_name),
    FOREIGN KEY (role_name) REFERENCES roles(name) ON DELETE CASCADE,
    FOREIGN KEY (permission_name) REFERENCES permissions(name) ON DELETE CASCADE
);
