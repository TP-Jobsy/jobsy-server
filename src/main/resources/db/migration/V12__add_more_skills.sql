COPY skills(name)
    FROM 'classpath:db/data/skills_to_import.csv'
    WITH (
    FORMAT csv,
    HEADER true,
    DELIMITER ','
    );