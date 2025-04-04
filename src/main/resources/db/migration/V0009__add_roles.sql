alter table users
    add column roles varchar(1024) not null default 'USER';