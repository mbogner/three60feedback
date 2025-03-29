CREATE TABLE feedback_responses
(
    id                uuid                        not null default uuid_generate_v4()
        constraint feedback_responses_pk primary key,
    created_at        timestamp without time zone not null default now_utc(),
    updated_at        timestamp without time zone
        constraint feedback_responses_cc__create_before_update check (created_at <= updated_at),
    lock_version      int                         not null default 0
        constraint feedback_responses_cc__lock_version_positive check (lock_version >= 0),

    feedback_round_id uuid                        not null
        constraint feedback_responses_fk__feedback_round_id references feedback_rounds (id) on delete restrict on update restrict,

    email             varchar(255)                not null,
    sent_at           timestamp without time zone,
    send_failed       boolean                     not null default false,
    positive_feedback text,
    negative_feedback text,
    notified_at       timestamp without time zone,
    notify_failed     boolean                     not null default false,
    reported          boolean                     not null default false,

    constraint feedback_responses_uc__feedback_round_id_email unique (feedback_round_id, email)
) WITHOUT OIDS;

CREATE TRIGGER feedback_responses_trg__check_created_at_unchanged
    BEFORE UPDATE
    ON feedback_responses
    FOR EACH ROW
EXECUTE PROCEDURE check_created_at_unchanged();

CREATE TRIGGER feedback_responses_trg__update_updated_at
    BEFORE UPDATE
    ON feedback_responses
    FOR EACH ROW
EXECUTE PROCEDURE update_updated_at();

CREATE TRIGGER feedback_responses_trg__increment_lock_version
    BEFORE UPDATE
    ON feedback_responses
    FOR EACH ROW
EXECUTE PROCEDURE increment_lock_version();
