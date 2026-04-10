create index idx_password_reset_tokens_user_id
    on password_reset_tokens(user_id);

create index idx_password_reset_tokens_expires_at
    on password_reset_tokens(expires_at);