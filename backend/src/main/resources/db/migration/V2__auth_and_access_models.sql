create table users
(
    id                    bigserial primary key,
    created_at            timestamp    not null,
    updated_at            timestamp    not null,
    username              varchar(255) not null unique,
    password              varchar(255) not null,
    email                 varchar(255) not null unique,
    role                  varchar(50)  not null,
    failed_login_attempts int     default 0,
    lock_level            int     default 0,
    locked_until          timestamp,
    notifications_enabled boolean default true
);

create table user_profiles
(
    user_id         bigserial primary key,
    name            varchar(255) not null,
    surname         varchar(255) not null,
    phone_number    varchar(255),
    street          varchar(255),
    city            varchar(255),
    postal_code     varchar(255),
    date_of_birth   date,
    gender          varchar(50),
    profile_picture_id bigint,
    foreign key (user_id) references users (id) on delete cascade,
    foreign key (profile_picture_id) references files (id) on delete set null
);

create table password_reset_tokens
(
    id             bigserial primary key,
    created_at     timestamp    not null,
    updated_at     timestamp    not null,
    user_id        bigserial    not null,
    token_hash     varchar(255) not null unique,
    expires_at     timestamp    not null,
    used_at        timestamp,
    invalidated_at timestamp,
    foreign key (user_id) references users (id) on delete cascade
);


create index idx_password_reset_tokens_user_id
    on password_reset_tokens (user_id);

create index idx_password_reset_tokens_expires_at
    on password_reset_tokens (expires_at);

create index idx_prt_active_by_user
    on password_reset_tokens (user_id, expires_at) where used_at is null
      and invalidated_at is null;
