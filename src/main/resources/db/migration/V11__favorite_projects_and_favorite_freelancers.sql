CREATE TABLE favorite_projects (
                                   freelancer_id BIGINT NOT NULL,
                                   project_id    BIGINT NOT NULL,
                                   created_at    TIMESTAMP NOT NULL DEFAULT NOW(),
                                   CONSTRAINT pk_favorite_projects PRIMARY KEY (freelancer_id, project_id),
                                   CONSTRAINT fk_favproj_freelancer FOREIGN KEY (freelancer_id)
                                       REFERENCES freelancer_profiles (id) ON DELETE CASCADE,
                                   CONSTRAINT fk_favproj_project FOREIGN KEY (project_id)
                                       REFERENCES projects (id) ON DELETE CASCADE
);

CREATE INDEX idx_favorite_projects_project ON favorite_projects (project_id);


CREATE TABLE favorite_freelancers (
                                      client_id     BIGINT NOT NULL,
                                      freelancer_id BIGINT NOT NULL,
                                      created_at    TIMESTAMP NOT NULL DEFAULT NOW(),
                                      CONSTRAINT pk_favorite_freelancers PRIMARY KEY (client_id, freelancer_id),
                                      CONSTRAINT fk_favfr_client FOREIGN KEY (client_id)
                                          REFERENCES client_profiles (id) ON DELETE CASCADE,
                                      CONSTRAINT fk_favfr_freelancer FOREIGN KEY (freelancer_id)
                                          REFERENCES freelancer_profiles (id) ON DELETE CASCADE
);

CREATE INDEX idx_favorite_freelancers_freelancer ON favorite_freelancers (freelancer_id);