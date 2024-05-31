--liquibase formatted sql

--changeset otto15:add_created_date

ALTER TABLE balance_operation
    ADD COLUMN created_date TIMESTAMP;
