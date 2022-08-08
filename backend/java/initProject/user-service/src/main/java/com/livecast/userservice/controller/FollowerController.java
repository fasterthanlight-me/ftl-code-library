package com.livecast.userservice.controller;

import com.livecast.userservice.entity.Follower;
import com.livecast.userservice.repository.FollowRepository;
import com.livecast.userservice.service.UserService;
import com.livecast.common.util.AuthUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RestController
@RequestMapping(path = "v1")
public class FollowerController {
    private final FollowRepository followRepository;
    private final UserService userService;

    public FollowerController(FollowRepository followRepository, UserService userService) {
        this.followRepository = followRepository;
        this.userService = userService;
    }

    @RequestMapping(path = "/follow/{userId}", method = RequestMethod.PUT)
    @Transactional
    public ResponseEntity<?> follow(Authentication authentication, @PathVariable Long userId){
        Long myId = userService.getIdByUsername(AuthUtil.getUsername(authentication));
        Follower follower = new Follower()
                .setFollowerId(myId)
                .setUserId(userId);
        followRepository.save(follower);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(path = "/unfollow/{userId}", method = RequestMethod.PUT)
    @Transactional
    public ResponseEntity<?> unfollow(Authentication authentication, @PathVariable Long userId){
        Long myId = userService.getIdByUsername(AuthUtil.getUsername(authentication));
        followRepository.deleteAllByFollowerIdAndUserId(myId, userId);
        return ResponseEntity.ok().build();
    }
}
