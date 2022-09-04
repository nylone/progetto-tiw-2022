create database if not exists progetto2022;
use progetto2022;

create user if not exists progetto2022_user@localhost identified by 'progetto2022_pass';
grant all privileges on progetto2022.* to progetto2022_user@localhost;
flush privileges;

create table if not exists user
(
    id    serial primary key,
    uname varchar(20)  not null unique,
    email varchar(255) not null unique,
    hash  binary(64)   not null,
    salt  binary(16)   not null
);

create table if not exists meeting
(
    id               serial primary key,
    title            varchar(50)     not null,
    date             date            not null,
    time             time            not null,
    duration         int unsigned    not null,
    max_participants int unsigned    not null,
    admin            bigint unsigned not null references user (id)
        on delete no action
        on update no action
);

create table if not exists meeting_invite
(
    id   serial primary key,
    m_id bigint unsigned not null references meeting (id)
        on delete no action
        on update no action,
    u_id bigint unsigned not null references user (id)
        on delete no action
        on update no action,
    constraint no_duplicate_invite unique (m_id, u_id)
);

create procedure assert_max_participants_constraint(in new_m_id bigint unsigned)
begin
    if (select count(*) from (select * from meeting_invite mi where new_m_id = mi.m_id) as `m*`)
        = (select max_participants from meeting where id = new_m_id)
    then
        signal sqlstate '45000' set message_text = "invites for selected meeting exceed the maximum amount";
    end if;
end;

create procedure assert_admin_invite_constraint(in new_m_id bigint unsigned, in new_u_id bigint unsigned)
begin
    if (select admin from meeting m where m.id = new_m_id) = new_u_id
    then
        signal sqlstate '45000' set message_text = "cannot invite an admin to its own meeting";
    end if;
end;

create trigger insert_max_participants_constraint
    before insert
    on meeting_invite
    for each row
    call assert_max_participants_constraint(new.m_id);

create trigger update_max_participants_constraint
    before update
    on meeting_invite
    for each row
    call assert_max_participants_constraint(new.m_id);

create trigger insert_admin_invite_constraint
    before insert
    on meeting_invite
    for each row
    call assert_admin_invite_constraint(new.m_id, new.u_id);

create trigger update_admin_invite_constraint
    before update
    on meeting_invite
    for each row
    call assert_admin_invite_constraint(new.m_id, new.u_id);