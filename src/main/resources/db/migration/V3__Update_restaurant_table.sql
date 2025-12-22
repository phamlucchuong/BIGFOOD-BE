alter table restaurants
    add column approved_at timestamp on update current_timestamp,
    add column request_at timestamp default current_timestamp;

update restaurants
set request_at = current_timestamp, approved_at = current_timestamp
where is_approved = true;