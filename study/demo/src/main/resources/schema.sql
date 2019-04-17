drop table t_user if exists;

create table t_user (
    id bigint auto_increment,
    create_time timestamp,
    update_time timestamp,
    name varchar(255),
    primary key (id)
);

insert into t_user (name, create_time, update_time) values ('espresso', now(), now());
insert into t_user (name, create_time, update_time) values ('latte', now(), now());
insert into t_user (name, create_time, update_time) values ('capuccino', now(), now());
insert into t_user (name, create_time, update_time) values ('mocha', now(), now());
insert into t_user (name, create_time, update_time) values ('macchiato', now(), now());