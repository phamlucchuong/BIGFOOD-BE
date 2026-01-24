alter table reviews
    add column sentiment varchar(20) default 'NEUTRAL' check (sentiment in ('POSITIVE', 'NEGATIVE', 'NEUTRAL'));