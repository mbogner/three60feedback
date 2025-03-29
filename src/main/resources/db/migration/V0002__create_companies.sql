CREATE TABLE companies
(
    id            uuid                        not null default uuid_generate_v4()
        constraint companies_pk primary key,
    created_at    timestamp without time zone not null default now_utc(),
    updated_at    timestamp without time zone
        constraint companies_cc__create_before_update check (created_at <= updated_at),
    lock_version  int                         not null default 0
        constraint companies_cc__lock_version_positive check (lock_version >= 0),

    name          varchar(255)                not null
        constraint companies_uc__name unique,
    domains       varchar(1024),

    mite_base_url varchar(255)                not null,
    mite_api_key  varchar(512)                 not null
) WITHOUT OIDS;

CREATE TRIGGER companies_trg__check_created_at_unchanged
    BEFORE UPDATE
    ON companies
    FOR EACH ROW
EXECUTE PROCEDURE check_created_at_unchanged();

CREATE TRIGGER companies_trg__update_updated_at
    BEFORE UPDATE
    ON companies
    FOR EACH ROW
EXECUTE PROCEDURE update_updated_at();

CREATE TRIGGER companies_trg__increment_lock_version
    BEFORE UPDATE
    ON companies
    FOR EACH ROW
EXECUTE PROCEDURE increment_lock_version();
