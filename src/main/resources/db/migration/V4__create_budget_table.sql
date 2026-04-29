CREATE TABLE budget (
    id BIGSERIAL PRIMARY KEY,
    amount DECIMAL(12,2) NOT NULL,
    month INT NOT NULL CHECK (month BETWEEN 1 AND 12),
    year INT NOT NULL,
    category_id BIGINT REFERENCES category(id),
    user_id BIGINT NOT NULL REFERENCES users(id)
);