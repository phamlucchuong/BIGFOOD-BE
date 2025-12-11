
-- Bảng User
CREATE TABLE users (
    id CHAR(36) PRIMARY KEY,          -- UUID lưu dạng chuỗi
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
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


create table food_categories (
    id char(36) primary key,
    name varchar(255) not null,
    icon_index int check (icon_index >= 0),
    is_deleted boolean default false,
    restaurant_id char(36),
    
    foreign key (restaurant_id) references restaurants(user_id)
);


create table foods (
    id char(36) primary key,
    name varchar(255) not null,
    description text,
    image_id varchar(255),
    price decimal(10, 2) not null check (price >= 0),
    count int default 0,
    is_deleted boolean default false,
    is_available boolean default true,
    food_category_id char(36),

    foreign key (food_category_id) references food_categories(id)
);


create table orders (
    id varchar(36) primary key,
    user_id varchar(36) not null,
    restaurant_id varchar(36) not null,
    status varchar(15) default 'PENDING' check (status in ('PENDING', 'CONFIRMED', 'PREPARING', 'DELIVERING', 'COMPLETED', 'CANCELLED', 'REJECTED')),
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp on update current_timestamp,
    delivery_address varchar(255) not null,
    delivery_latitude decimal(10,8) not null,
    delivery_longitude decimal(11,8) not null,
    delivery_distance decimal(10,2) not null check (delivery_distance >= 0),
    delivery_fee decimal(10,2) not null check (delivery_fee >= 0),
    total_amount decimal(10,2) not null check (total_amount >= 0),
    payment_method varchar(50) not null check (payment_method in ('MOMO', 'BANK', 'CASH_ON_DELIVERY')),
    notes text,
    cancell_reason text,
    reject_reason text,

    foreign key (user_id) references users(id),
    foreign key (restaurant_id) references restaurants(user_id)
);


create table order_details (
    id varchar(36) primary key,
    order_id varchar(36) not null,
    food_id varchar(36) not null,
    food_name varchar(100) not null,
    quantity int not null check (quantity > 0),
    unit_price decimal(10,2) not null check (unit_price >= 0),
    total_price decimal(10,2) not null check (total_price >= 0),
    notes text,

    foreign key (order_id) references orders(id),
    foreign key (food_id) references foods(id)
);


create table reviews (
    id varchar(36) primary key,
    rating int check (rating >= 1 and rating <= 5),
    review_text text,
    last_update_at timestamp default current_timestamp,
    reply_text text,
    reply_at timestamp,
    is_deleted boolean default false,
    order_id varchar(36),

    foreign key (order_id) references orders(id)
);
