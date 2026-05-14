ALTER TABLE staff
ADD CONSTRAINT uk_staff_user UNIQUE (user_id);
