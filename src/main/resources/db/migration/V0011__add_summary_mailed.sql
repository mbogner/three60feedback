alter table feedback_rounds
    add column summary        text,
    add column summary_mailed boolean not null default false;
