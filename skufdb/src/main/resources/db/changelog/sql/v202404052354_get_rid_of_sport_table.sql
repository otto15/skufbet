--liquibase formatted sql

--changeset dmitribazilov:get_rid_of_sport_table

ALTER TABLE event
    DROP COLUMN sport_id;

DROP TABLE sport;

ALTER TABLE event
    ADD COLUMN sport VARCHAR(255);

UPDATE event
SET sport = 'Dota 2'
WHERE id = 1;

UPDATE event
SET sport = 'Counter Strike 2'
WHERE id = 2;
