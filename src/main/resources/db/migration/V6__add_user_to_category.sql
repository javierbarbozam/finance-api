
ALTER TABLE category ADD COLUMN user_id BIGINT NOT NULL;

ALTER TABLE category
    ADD CONSTRAINT fk_category_user
    FOREIGN KEY (user_id)
    REFERENCES users(id);