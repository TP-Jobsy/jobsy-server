INSERT INTO users (
    email,
    password,
    role,
    first_name,
    last_name,
    date_birth,
    phone,
    is_verified,
    is_active,
    created_at,
    updated_at
) VALUES (
             'test@gmail.com',
             '$2a$10$D9w2Z2nF1qVtYbMOFZ.xwOxCW3U6iTQyZ7bpCvQyHZnL8W3Z0FC0e',
             'ADMIN',
             'Test',
             'Admin',
             '1987-09-23',
             '79151234567',
             TRUE,
             TRUE,
             NOW(),
             NOW()
         );