ALTER TABLE ai_requests
DROP CONSTRAINT fk_ai_project,
  ADD CONSTRAINT fk_ai_project
    FOREIGN KEY (project_id)
    REFERENCES projects(id)
    ON DELETE CASCADE;