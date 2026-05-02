-- V__create_request_management_tables.sql

CREATE TABLE requests
(
    id              BIGSERIAL PRIMARY KEY,

    title           VARCHAR(255),
    description     VARCHAR(2000),

    latitude        NUMERIC(10, 7),
    longitude       NUMERIC(10, 7),
    address_text    VARCHAR(255),

    summary         VARCHAR(2000),

    priority        VARCHAR(50),
    status          VARCHAR(50),
    routing_status  VARCHAR(50),

    user_id         BIGINT,
    municipality_id BIGINT,
    category_id     BIGINT,
    department_id   BIGINT,

    created_at      TIMESTAMP,
    updated_at      TIMESTAMP,

    CONSTRAINT fk_requests_user
        FOREIGN KEY (user_id) REFERENCES users (id),

    CONSTRAINT fk_requests_municipality
        FOREIGN KEY (municipality_id) REFERENCES municipalities (id),

    CONSTRAINT fk_requests_category
        FOREIGN KEY (category_id) REFERENCES categories (id),

    CONSTRAINT fk_requests_department
        FOREIGN KEY (department_id) REFERENCES departments (id),

    CONSTRAINT chk_requests_latitude
        CHECK (latitude IS NULL OR latitude BETWEEN -90 AND 90),

    CONSTRAINT chk_requests_longitude
        CHECK (longitude IS NULL OR longitude BETWEEN -180 AND 180)
);


CREATE TABLE request_assignments
(
    id                  BIGSERIAL PRIMARY KEY,

    request_id          BIGINT    NOT NULL,
    employee_user_id    BIGINT    NOT NULL,
    assigned_by_user_id BIGINT    NOT NULL,
    assigned_at         TIMESTAMP NOT NULL,

    CONSTRAINT fk_request_assignments_request
        FOREIGN KEY (request_id) REFERENCES requests (id),

    CONSTRAINT fk_request_assignments_employee
        FOREIGN KEY (employee_user_id) REFERENCES users (id),

    CONSTRAINT fk_request_assignments_assigned_by
        FOREIGN KEY (assigned_by_user_id) REFERENCES users (id)
);


CREATE TABLE request_comments
(
    id          BIGSERIAL PRIMARY KEY,

    request_id  BIGINT        NOT NULL,
    author_id   BIGINT        NOT NULL,

    content     VARCHAR(2000) NOT NULL,
    is_internal BOOLEAN       NOT NULL DEFAULT FALSE,

    created_at  TIMESTAMP,
    updated_at  TIMESTAMP,

    CONSTRAINT fk_request_comments_request
        FOREIGN KEY (request_id) REFERENCES requests (id),

    CONSTRAINT fk_request_comments_author
        FOREIGN KEY (author_id) REFERENCES users (id)
);


CREATE TABLE request_files
(
    id         BIGSERIAL PRIMARY KEY,

    request_id BIGINT NOT NULL,
    file_id    BIGINT NOT NULL,

    created_at TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_request_files_request
        FOREIGN KEY (request_id) REFERENCES requests (id),

    CONSTRAINT fk_request_files_file
        FOREIGN KEY (file_id) REFERENCES files (id),

    CONSTRAINT uq_request_files_request_file
        UNIQUE (request_id, file_id)
);


CREATE TABLE request_logs
(
    id                BIGSERIAL PRIMARY KEY,

    request_id        BIGINT       NOT NULL,
    action_by_user_id BIGINT       NOT NULL,

    action            VARCHAR(100) NOT NULL,
    old_value         VARCHAR(255),
    new_value         VARCHAR(255),
    note              VARCHAR(255),

    created_at        TIMESTAMP    NOT NULL,

    CONSTRAINT fk_request_logs_request
        FOREIGN KEY (request_id) REFERENCES requests (id),

    CONSTRAINT fk_request_logs_action_by
        FOREIGN KEY (action_by_user_id) REFERENCES users (id)
);


CREATE INDEX idx_requests_user_id
    ON requests (user_id);

CREATE INDEX idx_requests_municipality_id
    ON requests (municipality_id);

CREATE INDEX idx_requests_category_id
    ON requests (category_id);

CREATE INDEX idx_requests_department_id
    ON requests (department_id);

CREATE INDEX idx_requests_status
    ON requests (status);

CREATE INDEX idx_requests_priority
    ON requests (priority);

CREATE INDEX idx_requests_routing_status
    ON requests (routing_status);


CREATE INDEX idx_request_assignments_request_id
    ON request_assignments (request_id);

CREATE INDEX idx_request_assignments_employee_user_id
    ON request_assignments (employee_user_id);

CREATE INDEX idx_request_assignments_assigned_by_user_id
    ON request_assignments (assigned_by_user_id);


CREATE INDEX idx_request_comments_request_id
    ON request_comments (request_id);

CREATE INDEX idx_request_comments_author_id
    ON request_comments (author_id);

CREATE INDEX idx_request_comments_is_internal
    ON request_comments (is_internal);


CREATE INDEX idx_request_files_request_id
    ON request_files (request_id);

CREATE INDEX idx_request_files_file_id
    ON request_files (file_id);


CREATE INDEX idx_request_logs_request_id
    ON request_logs (request_id);

CREATE INDEX idx_request_logs_action_by_user_id
    ON request_logs (action_by_user_id);

CREATE INDEX idx_request_logs_action
    ON request_logs (action);

CREATE INDEX idx_request_logs_created_at
    ON request_logs (created_at);