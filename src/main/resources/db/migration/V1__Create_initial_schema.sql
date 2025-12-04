
-- Bảng User
CREATE TABLE users (
    id CHAR(36) PRIMARY KEY,          -- UUID lưu dạng chuỗi
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    image_id VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
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


create table history_search(
    id CHAR(36) PRIMARY KEY,          -- UUID lưu dạng chuỗi
    content VARCHAR(255) UNIQUE NOT NULL,
    count INT DEFAULT 1 NOT NULL,
    last_searched_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL
);

create table restaurant_categories (
    id char(36) primary key,
    name varchar(255) not null
);

CREATE TABLE restaurants (
    user_id CHAR(36) NOT NULL,
    restaurant_name VARCHAR(255) NOT NULL unique,
    address VARCHAR(255) NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    banner_id VARCHAR(255),
    license_id VARCHAR(255) NOT NULL,
    is_approved BOOLEAN DEFAULT FALSE,
    
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

create table restaurant_has_categories (
    restaurant_id char(36),
    restaurant_category_id char(36),

    primary key (restaurant_id, restaurant_category_id),
    foreign key (restaurant_id) references restaurants(user_id),
    foreign key (restaurant_category_id) references restaurant_categories(id)
);

create table ratings (
    id char(36) primary key,
    score int check (score >= 1 and score <= 5),
    comment text,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp,
    is_deleted boolean default false,
    user_id char(36) not null,
    restaurant_id char(36) not null,

    foreign key (user_id) references users(id),
    foreign key (restaurant_id) references restaurants(user_id)
);

create table food_categories (
    id char(36) primary key,
    name varchar(255) not null,
    restaurant_id char(36),
    
    foreign key (restaurant_id) references restaurants(user_id)
);

create table foods (
    id char(36) primary key,
    name varchar(255) not null,
    description text,
    image_url varchar(255),
    price decimal(10, 2) not null check (price >= 0),
    count int default 0,
    is_deleted boolean default false,
    food_category_id char(36),

    foreign key (food_category_id) references food_categories(id)
);
