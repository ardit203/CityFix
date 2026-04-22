create table files
(
    id         bigserial primary key,
    created_at timestamp    not null,
    updated_at timestamp    not null,
    file_name  varchar(255) not null unique,
    file_type  varchar(255) not null,
    file_url   varchar(255) not null unique,
    fileSize   bigserial    not null
);