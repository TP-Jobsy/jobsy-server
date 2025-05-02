COPY skills(name)
    FROM '/var/lib/postgresql/data/db/data/skills_to_import.csv'
    WITH (FORMAT csv,HEADER true,DELIMITER ',');