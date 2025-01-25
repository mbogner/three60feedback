CREATE TABLE users
(
    id           uuid                        not null default uuid_generate_v4()
        constraint users_pk primary key,
    created_at   timestamp without time zone not null default now_utc(),
    updated_at   timestamp without time zone
        constraint users_cc__create_before_update check (created_at <= updated_at),
    lock_version int                         not null default 0
        constraint users_cc__lock_version_positive check (lock_version >= 0),

    email        varchar(255)                not null
        constraint users_uc__name unique,
    company_id   uuid                        not null
        constraint users_fk__company_id references companies (id)
) WITHOUT OIDS;

CREATE TRIGGER users_trg__check_created_at_unchanged
    BEFORE UPDATE
    ON users
    FOR EACH ROW
EXECUTE PROCEDURE check_created_at_unchanged();

CREATE TRIGGER users_trg__update_updated_at
    BEFORE UPDATE
    ON users
    FOR EACH ROW
EXECUTE PROCEDURE update_updated_at();

CREATE TRIGGER users_trg__increment_lock_version
    BEFORE UPDATE
    ON users
    FOR EACH ROW
EXECUTE PROCEDURE increment_lock_version();
