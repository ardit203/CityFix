create index idx_password_reset_tokens_user_id
    on password_reset_tokens(user_id);

create index idx_password_reset_tokens_expires_at
    on password_reset_tokens(expires_at);

create index idx_prt_active_by_user
    on password_reset_tokens (user_id, expires_at)
    where used_at is null
      and invalidated_at is null;