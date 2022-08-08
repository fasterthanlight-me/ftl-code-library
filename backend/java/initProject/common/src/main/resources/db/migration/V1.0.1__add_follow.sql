create table followers
(
    user_id bigserial not null
        constraint followers_users_user_id_fk
            references users,
    follower_id bigserial not null
        constraint followers_users_follower_id_fk
            references users,
    followed_at timestamp,
    PRIMARY KEY (user_id, follower_id)
)