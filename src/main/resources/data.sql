insert into USERS (`nickname`, `password`) values('user1', '1234');
insert into USERS (`nickname`, `password`) values('user2', '1234');
insert into USERS (`nickname`, `password`) values('user3', '1234');
insert into ROOM (`id`, `title`) values(1000000, 'room1');
insert into ROOM (`id`, `title`) values(1000001, 'room2');
insert into ROOM_USER_DATA (`id`, `room_id`, `user_nickname`, `check_time`, `background_color`)
values(-1, 1000000, 'user1', now(), 'blue');
insert into ROOM_USER_DATA (`id`, `room_id`, `user_nickname`, `check_time`, `background_color`)
values(-2, 1000000, 'user2', now(), 'blue');
insert into ROOM_USER_DATA (`id`, `room_id`, `user_nickname`, `check_time`, `background_color`)
values(-3, 1000001, 'user1', now(), 'blue');
insert into ROOM_USER_DATA (`id`, `room_id`, `user_nickname`, `check_time`, `background_color`)
values(-4, 1000001, 'user3', now(), 'blue');
