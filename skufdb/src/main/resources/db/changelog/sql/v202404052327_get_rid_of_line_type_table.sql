--liquibase formatted sql

--changeset dmitribazilov:get_rid_of_line_type_table

ALTER TABLE line
    DROP COLUMN line_type_id;

DROP TABLE line_type;

ALTER TABLE line
    ADD COLUMN type VARCHAR(255);

UPDATE line
SET type = 'TOTAL'
WHERE id IN (1, 3);

UPDATE line
SET type = 'SCORE'
WHERE id IN (2, 4)
