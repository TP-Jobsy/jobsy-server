CREATE TABLE skills (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE freelancer_skills (
                                   freelancer_id BIGINT NOT NULL,
                                   skill_id BIGINT NOT NULL,
                                   PRIMARY KEY (freelancer_id, skill_id),
                                   CONSTRAINT fk_freelancer FOREIGN KEY (freelancer_id) REFERENCES freelancer_profiles (id) ON DELETE CASCADE,
                                   CONSTRAINT fk_skill FOREIGN KEY (skill_id) REFERENCES skills (id) ON DELETE CASCADE
);

CREATE TABLE freelancer_portfolio (
                                      id BIGSERIAL PRIMARY KEY,
                                      freelancer_id BIGINT NOT NULL,
                                      title VARCHAR(200) NOT NULL,
                                      description TEXT NOT NULL,
                                      role_in_project VARCHAR(100),
                                      project_link VARCHAR(255),
                                      created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                                      updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
                                      CONSTRAINT fk_freelancer_portfolio FOREIGN KEY (freelancer_id) REFERENCES freelancer_profiles (id) ON DELETE CASCADE
);

CREATE TABLE portfolio_skills (
                                  portfolio_id BIGINT NOT NULL,
                                  skill_id BIGINT NOT NULL,
                                  PRIMARY KEY (portfolio_id, skill_id),
                                  CONSTRAINT fk_portfolio FOREIGN KEY (portfolio_id) REFERENCES freelancer_portfolio (id) ON DELETE CASCADE,
                                  CONSTRAINT fk_skill_portfolio FOREIGN KEY (skill_id) REFERENCES skills (id) ON DELETE CASCADE
);