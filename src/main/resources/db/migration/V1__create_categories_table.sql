-- =====================================================
-- V1: Create categories table
-- =====================================================

CREATE TABLE categories (
    id              BIGSERIAL       PRIMARY KEY,
    name            VARCHAR(50)     NOT NULL,
    type            VARCHAR(10)     NOT NULL CHECK (type IN ('INCOME', 'EXPENSE')),
    icon            VARCHAR(50),
    color           VARCHAR(7),
    created_at      TIMESTAMP       NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP       NOT NULL DEFAULT NOW(),

    CONSTRAINT uq_categories_name_type UNIQUE (name, type)
);

-- Index for filtering by type
CREATE INDEX idx_categories_type ON categories(type);
