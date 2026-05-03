-- =====================================================
-- V4: Create users table
-- =====================================================

CREATE TABLE users (
    id              BIGSERIAL       PRIMARY KEY,
    first_name      VARCHAR(50)     NOT NULL,
    last_name       VARCHAR(50)     NOT NULL,
    email           VARCHAR(100)    NOT NULL UNIQUE,
    password        VARCHAR(255)    NOT NULL,
    role            VARCHAR(10)     NOT NULL DEFAULT 'USER' CHECK (role IN ('USER', 'ADMIN')),
    created_at      TIMESTAMP       NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP       NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_users_email ON users(email);

-- Add foreign key from transactions to users
ALTER TABLE transactions
    ADD CONSTRAINT fk_transactions_user
    FOREIGN KEY (user_id) REFERENCES users(id);
