create table login_user
(
    id       int auto_increment
        primary key,
    name     char(30) charset utf8 not null,
    password char(255)             not null,
    phone    char(20)              null,
    email    char(255)             null,
    avatar   char(255)             null,
    constraint login_user_name_uindex
        unique (name)
);


