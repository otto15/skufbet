--liquibase formatted sql

--changeset rama:add role column

ALTER TABLE user_profile
    ADD COLUMN role varchar(255);

ALTER TABLE user_profile
    ADD COLUMN keycloak_id varchar(255);
