--liquibase formatted sql

--changeset otto15:drop_user_profile_details_id_column

alter table user_profile_details drop column id;
