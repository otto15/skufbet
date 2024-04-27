--liquibase formatted sql

--changeset rama:add role column

CREATE SEQUENCE IF NOT EXISTS user_profile_id_seq as integer;
