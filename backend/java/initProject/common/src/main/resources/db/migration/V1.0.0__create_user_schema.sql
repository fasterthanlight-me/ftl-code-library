create table users
(
    id           bigserial PRIMARY KEY,
    username     varchar     not null unique,
    email        varchar     not null,
    bio          varchar,
    phone_number varchar,
    age          smallint,
    type         varchar(20) not null
);

create index users_username_idx on users (username);
create index users_email_idx on users (email);