--liquibase formatted sql

--changeset dmitribazilov:add_constraints

ALTER TABLE user_profile_details
    ADD CONSTRAINT passport_unq UNIQUE(passport);

ALTER TABLE user_profile_details
    ADD CONSTRAINT taxpayer_unq UNIQUE(taxpayer_id);

ALTER TABLE bet
    ALTER COLUMN coefficient SET NOT NULL;

ALTER TABLE event
    ALTER COLUMN event_name SET NOT NULL,
    ALTER COLUMN is_end SET NOT NULL,
    ALTER COLUMN sport SET NOT NULL;

ALTER TABLE line
    ALTER COLUMN is_closed SET NOT NULL,
    ALTER COLUMN type SET NOT NULL;

ALTER TABLE line_result
    ALTER COLUMN result SET NOT NULL,
    ALTER COLUMN coefficient SET NOT NULL;

ALTER TABLE tournament
    ALTER COLUMN tournament_name SET NOT NULL,
    ALTER COLUMN is_end SET NOT NULL;

ALTER TABLE user_profile
    ALTER COLUMN mail SET NOT NULL,
    ALTER COLUMN phone_number SET NOT NULL,
    ALTER COLUMN password SET NOT NULL,
    ALTER COLUMN balance SET NOT NULL;

ALTER TABLE user_profile_details
    ALTER COLUMN first_name SET NOT NULL,
    ALTER COLUMN last_name SET NOT NULL,
    ALTER COLUMN passport SET NOT NULL,
    ALTER COLUMN date_of_birth SET NOT NULL,
    ALTER COLUMN taxpayer_id SET NOT NULL;
