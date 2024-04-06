--liquibase formatted sql

--changeset dmitribazilov:change_personal_user_profile

insert into tournament (id, tournament_name, is_end)
values (default, 'International 2024', false);
insert into tournament (id, tournament_name, is_end)
values (default, 'Berlin Major', false);

insert into event (id, tournament_id, sport_id, event_name, is_end)
values (default, 1, null, 'Navi - Liquid', false);
insert into event (id, tournament_id, sport_id, event_name, is_end)
values (default, 2, null, 'Mouz - Astralis', false);
