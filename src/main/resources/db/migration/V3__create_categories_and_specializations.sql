CREATE TABLE categories (
                            id BIGSERIAL PRIMARY KEY,
                            name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE specializations (
                                 id BIGSERIAL PRIMARY KEY,
                                 category_id BIGINT NOT NULL,
                                 name VARCHAR(100) NOT NULL,
                                 CONSTRAINT fk_specialization_category FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE
);