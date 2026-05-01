-- =====================================================
-- V3: Seed default categories
-- =====================================================

-- Expense categories
INSERT INTO categories (name, type, icon, color) VALUES
    ('Food & Dining',       'EXPENSE', '🍔', '#FF6B6B'),
    ('Transportation',      'EXPENSE', '🚗', '#4ECDC4'),
    ('Housing',             'EXPENSE', '🏠', '#45B7D1'),
    ('Utilities',           'EXPENSE', '💡', '#96CEB4'),
    ('Entertainment',       'EXPENSE', '🎬', '#DDA0DD'),
    ('Healthcare',          'EXPENSE', '🏥', '#98D8C8'),
    ('Shopping',            'EXPENSE', '🛍️', '#F7DC6F'),
    ('Education',           'EXPENSE', '📚', '#BB8FCE'),
    ('Personal Care',       'EXPENSE', '💇', '#F0B27A'),
    ('Insurance',           'EXPENSE', '🛡️', '#85C1E9'),
    ('Subscriptions',       'EXPENSE', '📱', '#F1948A'),
    ('Other Expenses',      'EXPENSE', '📦', '#AEB6BF');

-- Income categories
INSERT INTO categories (name, type, icon, color) VALUES
    ('Salary',              'INCOME', '💰', '#2ECC71'),
    ('Freelance',           'INCOME', '💻', '#27AE60'),
    ('Investments',         'INCOME', '📈', '#1ABC9C'),
    ('Gifts',               'INCOME', '🎁', '#F39C12'),
    ('Other Income',        'INCOME', '💵', '#16A085');
