create table login_user
(
    id              int auto_increment
        primary key,
    name            char(30) charset utf8 not null,
    password        char(255)             not null,
    phone           char(20)              null,
    email           char(255)             null,
    avatar          char(255)             null,
    background      char(255)             null,
    backgroundColor int                   null,
    constraint login_user_email_uindex
        unique (email),
    constraint login_user_name_uindex
        unique (name),
    constraint login_user_phone_uindex
        unique (phone)
);