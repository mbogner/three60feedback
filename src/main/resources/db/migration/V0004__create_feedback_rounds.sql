CREATE TABLE feedback_rounds
(
    id           uuid                        not null default uuid_generate_v4()
        constraint feedback_rounds_pk primary key,
    created_at   timestamp without time zone not null default now_utc(),
    updated_at   timestamp without time zone
        constraint feedback_rounds_cc__create_before_update check (created_at <= updated_at),
    lock_version int                         not null default 0
        constraint feedback_rounds_cc__lock_version_positive check (lock_version >= 0),

    receiver_id  uuid                        not null
        constraint feedback_rounds_fk__receiver_id references users (id) on delete restrict on update restrict,
    validity     timestamp without time zone not null default now_utc() + interval '7 days'
) WITHOUT OIDS;

CREATE TRIGGER feedback_rounds_trg__check_created_at_unchanged
    BEFORE UPDATE
    ON feedback_rounds
    FOR EACH ROW
EXECUTE PROCEDURE check_created_at_unchanged();

CREATE TRIGGER feedback_rounds_trg__update_updated_at
    BEFORE UPDATE
    ON feedback_rounds
    FOR EACH ROW
EXECUTE PROCEDURE update_updated_at();

CREATE TRIGGER feedback_rounds_trg__increment_lock_version
    BEFORE UPDATE
    ON feedback_rounds
    FOR EACH ROW
EXECUTE PROCEDURE increment_lock_version();
