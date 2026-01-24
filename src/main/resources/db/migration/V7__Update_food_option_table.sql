
alter table food_options
    add column is_default_price BOOLEAN not null default false;