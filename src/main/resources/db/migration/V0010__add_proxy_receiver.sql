alter table feedback_rounds
    add column proxy_receiver_id uuid
        constraint feedback_rounds_fk__proxy_receiver_id references users (id) on delete restrict on update restrict;