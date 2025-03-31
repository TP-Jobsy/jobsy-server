CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role user_role_enum NOT NULL,
                       first_name VARCHAR(50) NOT NULL,
                       last_name VARCHAR(50) NOT NULL,
                       phone VARCHAR(30),
                       is_verified BOOLEAN DEFAULT FALSE,
                       verification_code VARCHAR(10),
                       is_active BOOLEAN DEFAULT TRUE,
                       created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                       updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE confirmations (
                               id BIGSERIAL PRIMARY KEY,
                               user_id BIGINT NOT NULL,
                               action confirmation_action_enum NOT NULL,
                               confirmation_code VARCHAR(10) NOT NULL,
                               expires_at TIMESTAMP NOT NULL,
                               used BOOLEAN DEFAULT FALSE,
                               created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                               CONSTRAINT fk_confirmation_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);