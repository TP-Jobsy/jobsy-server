DELETE FROM users
WHERE role = 'ADMIN'
  AND email = 'mora.sine@yandex.ru';

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
             'mora.sine@yandex.ru',
             '$2a$10$XXXXXXXXXXXXXYYYYYYYYYYYYZZZZZZ',
             'ADMIN',
             'Mora',
             'Sine',
             '2000-01-01',
             '79123456789',
             TRUE,
             TRUE,
             NOW(),
             NOW()
         );