--liquibase formatted sql

--changeset dmitribazilov:add_sample_line_data
insert into line_type (id, type_name)
values (default, 'TOTAL');
insert into line_type (id, type_name)
values (default, 'SCORE');

insert into line (id, event_id, line_type_id, result_id, is_closed)
values (default, 1, 1, null, false);
insert into line (id, event_id, line_type_id, result_id, is_closed)
values (default, 1, 2, null, false);
insert into line (id, event_id, line_type_id, result_id, is_closed)
values (default, 2, 1, null, false);
insert into line (id, event_id, line_type_id, result_id, is_closed)
values (default, 2, 2, null, false);

insert into line_result (id, line_id, result, coefficient)
values (default, 1, '>4.5', 2.4);
insert into line_result (id, line_id, result, coefficient)
values (default, 1, '<4.5', 1.5);

insert into line_result (id, line_id, result, coefficient)
values (default, 2, '2 : 0', 6);
insert into line_result (id, line_id, result, coefficient)
values (default, 2, '0 : 2', 2.6);
insert into line_result (id, line_id, result, coefficient)
values (default, 2, '2 : 1', 4.5);
insert into line_result (id, line_id, result, coefficient)
values (default, 2, '1 : 2', 1.9);

insert into line_result (id, line_id, result, coefficient)
values (default, 3, '>4.5', 2.8);
insert into line_result (id, line_id, result, coefficient)
values (default, 3, '<4.5', 1.24);

insert into line_result (id, line_id, result, coefficient)
values (default, 4, '2 : 0', 3);
insert into line_result (id, line_id, result, coefficient)
values (default, 4, '0 : 2', 1.6);
insert into line_result (id, line_id, result, coefficient)
values (default, 4, '2 : 1', 2.4);
insert into line_result (id, line_id, result, coefficient)
values (default, 4, '1 : 2', 1.35);
