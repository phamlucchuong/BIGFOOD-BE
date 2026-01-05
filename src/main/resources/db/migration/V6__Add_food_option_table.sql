
create table food_options (
    id VARCHAR(36) primary key,
    name VARCHAR(255) not null,
    price DECIMAL(10,2) not null,
    is_default BOOLEAN not null default false,
    food_id VARCHAR(36) not null,

    foreign key (food_id) references foods(id)
);



alter table foods
    drop column price;