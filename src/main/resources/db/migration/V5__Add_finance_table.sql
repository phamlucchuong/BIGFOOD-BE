create table finances (
    order_id varchar(36) not null,
    total_amount decimal(10,2) not null check (total_amount >= 0),
    admin_profit decimal(10,2) not null check (admin_profit >= 0),
    restaurant_profit decimal(10,2) not null check (restaurant_profit >= 0),
    status varchar(20) default 'PENDING' check (status in ('PENDING', 'COMPLETED', 'FAILED')),
    primary key (order_id),
    foreign key (order_id) references orders(id)
);