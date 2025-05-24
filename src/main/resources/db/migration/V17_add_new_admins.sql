DELETE
FROM users
WHERE role = 'ADMIN';

INSERT INTO users (email,
                   password,
                   role,
                   first_name,
                   last_name,
                   date_birth,
                   phone,
                   is_verified,
                   is_active,
                   created_at,
                   updated_at)
VALUES ('mora.sine@yandex.ru',
        '$2a$10$XXXXXXXXXXXXXYYYYYYYYYYYYZZZZZZZZZZZZZZZZZZZZZZZ',
        'ADMIN',
        'Mora',
        'Sine',
        '2000-01-01',
        NULL,
        TRUE,
        TRUE,
        NOW(),
        NOW());