create table notifications
(
    id                  bigserial primary key,
    recipient_id        bigint        not null,
    type                varchar(50)   not null,
    subject             varchar(255)  not null,
    message             varchar(3000) not null,
    status              varchar(50)   not null default 'PENDING',
    sent_at             timestamp,
    provider_message_id varchar(255),
    failure_reason      varchar(1000),
    retry_count         integer       not null default 0,
    last_tried_at       timestamp,
    created_at          timestamp,
    updated_at          timestamp,

    constraint fk_notifications_recipient
        foreign key (recipient_id)
            references users (id)
            on delete cascade
);