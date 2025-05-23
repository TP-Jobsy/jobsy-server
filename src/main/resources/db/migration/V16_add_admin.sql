ALTER TYPE confirmation_action_enum ADD VALUE 'ADMIN_LOGIN';

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
) VALUES
      (
          'vds_dasha@icloud.com',
          '$2a$10$XXXXXXXXXXXXXYYYYYYYYYYYYZZZZZZZZZZZZZZZZZZZZZZZ',
          'ADMIN',
          'System',
          'Administrator',
          '1970-01-01',
          NULL,
          TRUE,
          TRUE,
          NOW(),
          NOW()
      ),
      (
          'arpine985985@gmail.com',
          '$2a$10$AAAAAAAAAAAABBBBBBBBBBBCCCCCCCCCCCCCCCCCCCCCC',
          'ADMIN',
          'Operations',
          'Team',
          '1975-01-01',
          NULL,
          TRUE,
          TRUE,
          NOW(),
          NOW()
      );