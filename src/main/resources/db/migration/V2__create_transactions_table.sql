-- =====================================================
-- V2: Create transactions table
-- =====================================================

CREATE TABLE transactions (
    id              BIGSERIAL       PRIMARY KEY,
    amount          DECIMAL(12,2)   NOT NULL CHECK (amount > 0),
    description     VARCHAR(255)    NOT NULL,
    date            DATE            NOT NULL,
    type            VARCHAR(10)     NOT NULL CHECK (type IN ('INCOME', 'EXPENSE')),
    category_id     BIGINT          NOT NULL REFERENCES categories(id),
    user_id         BIGINT          NOT NULL,
    created_at      TIMESTAMP       NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP       NOT NULL DEFAULT NOW()
);

-- Indexes for common query patterns
CREATE INDEX idx_transactions_user_id ON transactions(user_id);
CREATE INDEX idx_transactions_date ON transactions(date);
CREATE INDEX idx_transactions_user_date ON transactions(user_id, date);
CREATE INDEX idx_transactions_category ON transactions(category_id);
