CREATE TABLE reviews (
                         id BIGSERIAL PRIMARY KEY,
                         project_id BIGINT NOT NULL,
                         reviewer_id BIGINT NOT NULL,
                         target_id BIGINT NOT NULL,
                         rating INT CHECK (rating BETWEEN 1 AND 5),
                         comment TEXT DEFAULT NULL,
                         created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                         CONSTRAINT fk_review_project FOREIGN KEY (project_id) REFERENCES projects (id) ON DELETE CASCADE,
                         CONSTRAINT fk_reviewer FOREIGN KEY (reviewer_id) REFERENCES users (id) ON DELETE CASCADE,
                         CONSTRAINT fk_target FOREIGN KEY (target_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE ai_requests (
                             id BIGSERIAL PRIMARY KEY,
                             user_id BIGINT NOT NULL,
                             project_id BIGINT NOT NULL,
                             system_prompt VARCHAR(2000),
                             user_prompt VARCHAR(2000),
                             output TEXT,
                             created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                             CONSTRAINT fk_ai_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
                             CONSTRAINT fk_ai_project FOREIGN KEY (project_id) REFERENCES projects (id) ON DELETE SET NULL
);

CREATE TABLE project_ai_history (
                                    id BIGSERIAL PRIMARY KEY,
                                    project_id BIGINT NOT NULL,
                                    ai_request_id BIGINT NOT NULL,
                                    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                                    CONSTRAINT fk_project FOREIGN KEY (project_id) REFERENCES projects (id) ON DELETE CASCADE,
                                    CONSTRAINT fk_ai_request FOREIGN KEY (ai_request_id) REFERENCES ai_requests (id) ON DELETE CASCADE
);