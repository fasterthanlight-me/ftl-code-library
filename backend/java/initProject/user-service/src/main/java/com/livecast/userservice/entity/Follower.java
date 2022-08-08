package com.livecast.userservice.entity;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Accessors(chain = true)
@Table(name = "followers")
@IdClass(FollowerId.class)
@Entity
public class Follower {
    @Id
    private Long userId;

    @Id
    private Long followerId;

    @CreationTimestamp
    private LocalDateTime followedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Follower follower = (Follower) o;
        return userId != null && Objects.equals(userId, follower.userId)
                && followerId != null && Objects.equals(followerId, follower.followerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, followerId);
    }
}
