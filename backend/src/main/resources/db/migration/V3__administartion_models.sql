create table municipalities
(
    id   bigserial primary key,
    name varchar(255) not null unique,
    code varchar(255) not null unique
);

create table departments
(
    id          bigserial primary key,
    name        varchar(255) not null unique,
    description text
);

create table categories
(
    id            bigserial primary key,
    name          varchar(255) not null unique,
    description   text,
    department_id bigserial    not null,
    foreign key (department_id) references departments (id) on delete cascade
);

create table staff
(
    id              bigserial primary key,
    user_id         bigserial not null,
    department_id   bigserial not null,
    municipality_id bigserial not null,
    foreign key (user_id) references users (id) on delete cascade,
    foreign key (department_id) references departments (id) on delete cascade,
    foreign key (municipality_id) references municipalities (id) on delete cascade
);

create index idx_categories_department_id on categories (department_id);
create index idx_staff_user_id on staff (user_id);
create index idx_staff_department_id on staff (department_id);
create index idx_staff_municipality_id on staff (municipality_id);
