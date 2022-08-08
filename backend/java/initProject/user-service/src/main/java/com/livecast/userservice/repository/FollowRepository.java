package com.livecast.userservice.repository;

import com.livecast.userservice.entity.Follower;
import com.livecast.userservice.entity.FollowerId;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FollowRepository extends PagingAndSortingRepository<Follower, FollowerId>, JpaSpecificationExecutor<Follower> {
    void deleteAllByUserId(Long userId);
    void deleteAllByFollowerId(Long followerId);
    void deleteAllByFollowerIdAndUserId(Long followerId, Long userId);
}
