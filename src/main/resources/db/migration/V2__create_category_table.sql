CREATE TABLE category (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(10) NOT NULL CHECK (type IN ('INCOME', 'EXPENSE')),
    color VARCHAR(7),
    icon VARCHAR(50),
    user_id BIGINT NOT NULL REFERENCES users(id)
);