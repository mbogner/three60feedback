CREATE TABLE feedback_requests
(
    id           uuid                        not null default uuid_generate_v4()
        constraint feedback_requests_pk primary key,
    created_at   timestamp without time zone not null default now_utc(),
    updated_at   timestamp without time zone
        constraint feedback_requests_cc__create_before_update check (created_at <= updated_at),
    lock_version int                         not null default 0
        constraint feedback_requests_cc__lock_version_positive check (lock_version >= 0),

    company_id   uuid                        not null
        constraint feedback_requests_fk__company_id references companies (id) on delete restrict on update restrict,
    email        varchar(255)                not null
) WITHOUT OIDS;

CREATE TRIGGER feedback_requests_trg__check_created_at_unchanged
    BEFORE UPDATE
    ON feedback_requests
    FOR EACH ROW
EXECUTE PROCEDURE check_created_at_unchanged();

CREATE TRIGGER feedback_requests_trg__update_updated_at
    BEFORE UPDATE
    ON feedback_requests
    FOR EACH ROW
EXECUTE PROCEDURE update_updated_at();

CREATE TRIGGER feedback_requests_trg__increment_lock_version
    BEFORE UPDATE
    ON feedback_requests
    FOR EACH ROW
EXECUTE PROCEDURE increment_lock_version();
