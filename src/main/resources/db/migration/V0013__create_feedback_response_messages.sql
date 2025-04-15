CREATE TABLE feedback_response_messages
(
    id                   uuid                        not null default uuid_generate_v4()
        constraint feedback_response_messages_pk primary key,
    created_at           timestamp without time zone not null default now_utc(),
    updated_at           timestamp without time zone
        constraint feedback_response_messages_cc__create_before_update check (created_at <= updated_at),
    lock_version         int                         not null default 0
        constraint feedback_response_messages_cc__lock_version_positive check (lock_version >= 0),

    feedback_response_id uuid                        not null
        constraint feedback_response_messages_fk__feedback_response_id references feedback_responses (id) on delete restrict on update restrict,

    sender_mail          varchar(255)                not null,
    message              text                        not null,
    message_sent_at      timestamp without time zone,
    message_send_fails   int                         not null default 0
) WITHOUT OIDS;

CREATE TRIGGER feedback_response_messages_trg__check_created_at_unchanged
    BEFORE UPDATE
    ON feedback_response_messages
    FOR EACH ROW
EXECUTE PROCEDURE check_created_at_unchanged();

CREATE TRIGGER feedback_response_messages_trg__update_updated_at
    BEFORE UPDATE
    ON feedback_response_messages
    FOR EACH ROW
EXECUTE PROCEDURE update_updated_at();

CREATE TRIGGER feedback_response_messages_trg__increment_lock_version
    BEFORE UPDATE
    ON feedback_response_messages
    FOR EACH ROW
EXECUTE PROCEDURE increment_lock_version();
