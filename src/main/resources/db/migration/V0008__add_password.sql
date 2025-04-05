alter table users
    add column password_hash                 varchar(1024),
    add column forgot_password_token         uuid
        constraint users_uc__forgot_password_token unique,
    add column forgot_password_created_at    timestamp without time zone,
    add column forgot_password_sent_at       timestamp without time zone,
    add column forgot_password_send_attempts int default 0
;