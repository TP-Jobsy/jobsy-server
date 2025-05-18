CREATE TABLE ratings (
                         id                   BIGSERIAL PRIMARY KEY,
                         project_id           BIGINT      NOT NULL
                             REFERENCES projects(id) ON DELETE CASCADE,
                         rater_freelancer_id  BIGINT NULL
        REFERENCES freelancer_profiles(id) ON DELETE CASCADE,
                         rater_client_id      BIGINT NULL
        REFERENCES client_profiles(id)     ON DELETE CASCADE,
                         target_freelancer_id BIGINT NULL
        REFERENCES freelancer_profiles(id) ON DELETE CASCADE,
                         target_client_id     BIGINT NULL
        REFERENCES client_profiles(id)     ON DELETE CASCADE,
                         score                SMALLINT NOT NULL CHECK (score BETWEEN 1 AND 5),
                         created_at           TIMESTAMP NOT NULL DEFAULT NOW()
);

ALTER TABLE freelancer_profiles
    ADD COLUMN IF NOT EXISTS average_rating DOUBLE PRECISION NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS rating_count   INTEGER         NOT NULL DEFAULT 0;

ALTER TABLE client_profiles
    ADD COLUMN IF NOT EXISTS average_rating DOUBLE PRECISION NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS rating_count   INTEGER         NOT NULL DEFAULT 0;

CREATE UNIQUE INDEX uq_ratings_per_project_freelancer
    ON ratings (project_id, rater_freelancer_id)
    WHERE rater_freelancer_id IS NOT NULL;

CREATE UNIQUE INDEX uq_ratings_per_project_client
    ON ratings (project_id, rater_client_id)
    WHERE rater_client_id IS NOT NULL;