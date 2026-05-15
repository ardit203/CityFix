CREATE TABLE ai_suggestions (
                                id BIGSERIAL PRIMARY KEY,

                                request_id BIGINT NOT NULL,
                                category_id BIGINT NOT NULL,

                                priority VARCHAR(50),
                                ai_summary VARCHAR(2000),
                                suggestion_status VARCHAR(50) NOT NULL DEFAULT 'PENDING_REVIEW',

                                created_at TIMESTAMP,
                                updated_at TIMESTAMP,

                                CONSTRAINT fk_ai_suggestions_request
                                    FOREIGN KEY (request_id)
                                        REFERENCES requests(id),

                                CONSTRAINT fk_ai_suggestions_category
                                    FOREIGN KEY (category_id)
                                        REFERENCES categories(id)
);

CREATE INDEX idx_ai_suggestions_request_id
    ON ai_suggestions(request_id);

CREATE INDEX idx_ai_suggestions_category_id
    ON ai_suggestions(category_id);

CREATE INDEX idx_ai_suggestions_status
    ON ai_suggestions(suggestion_status);