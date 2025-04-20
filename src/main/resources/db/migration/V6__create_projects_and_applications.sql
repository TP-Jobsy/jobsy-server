CREATE TABLE projects (
                          id BIGSERIAL PRIMARY KEY,
                          client_id BIGINT NOT NULL,
                          title VARCHAR(200) NOT NULL,
                          description TEXT NOT NULL,
                          category_id BIGINT,
                          specialization_id BIGINT,
                          complexity complexity_enum NOT NULL,
                          payment_type payment_type_enum NOT NULL DEFAULT 'FIXED',
                          fixed_price NUMERIC(10,2),
                          duration project_duration_enum,
                          status project_status_enum NOT NULL DEFAULT 'OPEN',
                          created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                          updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
                          CONSTRAINT fk_project_client FOREIGN KEY (client_id) REFERENCES client_profiles (id) ON DELETE CASCADE,
                          CONSTRAINT fk_project_category FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE SET NULL,
                          CONSTRAINT fk_project_specialization FOREIGN KEY (specialization_id) REFERENCES specializations (id) ON DELETE SET NULL
);

ALTER TABLE projects
    ADD COLUMN assigned_freelancer_id BIGINT;

ALTER TABLE projects
    ADD CONSTRAINT fk_assigned_freelancer FOREIGN KEY (assigned_freelancer_id) REFERENCES freelancer_profiles (id) ON DELETE SET NULL;

CREATE TABLE project_skills (
                                project_id BIGINT NOT NULL,
                                skill_id BIGINT NOT NULL,
                                PRIMARY KEY (project_id, skill_id),
                                CONSTRAINT fk_project FOREIGN KEY (project_id) REFERENCES projects (id) ON DELETE CASCADE,
                                CONSTRAINT fk_skill_project FOREIGN KEY (skill_id) REFERENCES skills (id) ON DELETE CASCADE
);

CREATE TABLE project_applications (
                                      id BIGSERIAL PRIMARY KEY,
                                      project_id BIGINT NOT NULL,
                                      freelancer_id BIGINT NOT NULL,
                                      status project_application_status_enum NOT NULL DEFAULT 'PENDING',
                                      application_type application_type_enum NOT NULL,
                                      created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                                      CONSTRAINT fk_app_project FOREIGN KEY (project_id) REFERENCES projects (id) ON DELETE CASCADE,
                                      CONSTRAINT fk_app_freelancer FOREIGN KEY (freelancer_id) REFERENCES freelancer_profiles (id) ON DELETE CASCADE
);