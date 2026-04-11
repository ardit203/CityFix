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
    locked_until          timestamp,
    notifications_enabled boolean default true
);

create table user_profiles
(
    user_id     bigserial primary key,
    created_at  timestamp    not null,
    updated_at  timestamp    not null,
    name        varchar(255) not null,
    surname     varchar(255) not null,
    street      varchar(255),
    city        varchar(255),
    postal_code varchar(255),
    foreign key (user_id) references users (id) on delete cascade
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
