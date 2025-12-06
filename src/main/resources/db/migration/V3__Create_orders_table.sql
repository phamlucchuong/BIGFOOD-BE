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
    delivery_fee decimal(10,2) not null,
    total_amount decimal(10,2) not null,
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