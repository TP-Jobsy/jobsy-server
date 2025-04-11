CREATE TABLE freelancer_profiles (
                                     id BIGSERIAL PRIMARY KEY,
                                     user_id BIGINT NOT NULL UNIQUE,
                                     hourly_rate NUMERIC(10,2),
                                     experience_level experience_enum,
                                     category_id BIGINT,
                                     specialization_id BIGINT,
                                     country VARCHAR(100),
                                     city VARCHAR(100),
                                     about_me TEXT,
                                     contact_link VARCHAR(255),
                                     created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                                     updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
                                     CONSTRAINT fk_freelancer_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE client_profiles (
                                 id BIGSERIAL PRIMARY KEY,
                                 user_id BIGINT NOT NULL UNIQUE,
                                 company_name VARCHAR(100),
                                 country VARCHAR(100),
                                 contact_link VARCHAR(255),
                                 city VARCHAR(100),
                                 position VARCHAR(100),
                                 field_description TEXT,
                                 created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                                 updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
                                 CONSTRAINT fk_client_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);