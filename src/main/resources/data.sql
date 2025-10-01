MERGE INTO friend_statuses AS target
USING (SELECT 1 AS id, 'ACCEPTED' AS status) AS source
ON (target.id = source.id)
WHEN NOT MATCHED THEN
INSERT (status) VALUES (source.status);

MERGE INTO friend_statuses AS target
USING (SELECT 2 AS id, 'PENDING' AS status) AS source
ON (target.id = source.id)
WHEN NOT MATCHED THEN
INSERT (status) VALUES (source.status);

MERGE INTO mpa AS target
USING (SELECT 1 AS id, 'G' AS name) AS source
ON (target.id = source.id)
WHEN NOT MATCHED THEN
INSERT (name) VALUES (source.name);

MERGE INTO mpa AS target
USING (SELECT 2 AS id, 'PG' AS name) AS source
ON (target.id = source.id)
WHEN NOT MATCHED THEN
INSERT (name) VALUES (source.name);

MERGE INTO mpa AS target
USING (SELECT 3 AS id, 'PG-13' AS name) AS source
ON (target.id = source.id)
WHEN NOT MATCHED THEN
INSERT (name) VALUES (source.name);

MERGE INTO mpa AS target
USING (SELECT 4 AS id, 'R' AS name) AS source
ON (target.id = source.id)
WHEN NOT MATCHED THEN
INSERT (name) VALUES (source.name);

MERGE INTO mpa AS target
USING (SELECT 5 AS id, 'NC-17' AS name) AS source
ON (target.id = source.id)
WHEN NOT MATCHED THEN
INSERT (name) VALUES (source.name);

MERGE INTO friend_statuses AS target
USING (SELECT 2 AS id, 'PENDING' AS status) AS source
ON (target.id = source.id)
WHEN NOT MATCHED THEN
INSERT (status) VALUES (source.status);

MERGE INTO genres AS target
USING (SELECT 1 AS id, 'Комедия' AS name) AS source
ON (target.id = source.id)
WHEN NOT MATCHED THEN
INSERT (name) VALUES (source.name);

MERGE INTO genres AS target
USING (SELECT 2 AS id, 'Драма' AS name) AS source
ON (target.id = source.id)
WHEN NOT MATCHED THEN
INSERT (name) VALUES (source.name);

MERGE INTO genres AS target
USING (SELECT 3 AS id, 'Мультфильм' AS name) AS source
ON (target.id = source.id)
WHEN NOT MATCHED THEN
INSERT (name) VALUES (source.name);

MERGE INTO genres AS target
USING (SELECT 4 AS id, 'Триллер' AS name) AS source
ON (target.id = source.id)
WHEN NOT MATCHED THEN
INSERT (name) VALUES (source.name);

MERGE INTO genres AS target
USING (SELECT 5 AS id, 'Документальный' AS name) AS source
ON (target.id = source.id)
WHEN NOT MATCHED THEN
INSERT (name) VALUES (source.name);

MERGE INTO genres AS target
USING (SELECT 6 AS id, 'Боевик' AS name) AS source
ON (target.id = source.id)
WHEN NOT MATCHED THEN
INSERT (name) VALUES (source.name);
