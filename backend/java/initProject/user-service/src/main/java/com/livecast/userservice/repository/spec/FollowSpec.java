package com.livecast.userservice.repository.spec;

import com.livecast.userservice.entity.Follower;
import com.livecast.userservice.entity.Follower_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class FollowSpec {
    public static Specification<Follower> userId(Long userId) {
        return new Specification<Follower>() {
            @Override
            public Predicate toPredicate(Root<Follower> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                query.distinct(true);
                return cb.equal(root.get(Follower_.USER_ID), userId);
            }
        };
    }

    public static Specification<Follower> followerId(Long followerId) {
        return new Specification<Follower>() {
            @Override
            public Predicate toPredicate(Root<Follower> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                query.distinct(true);
                return cb.equal(root.get(Follower_.FOLLOWER_ID), followerId);
            }
        };
    }
}
