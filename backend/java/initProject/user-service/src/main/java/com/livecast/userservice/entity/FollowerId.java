package com.livecast.userservice.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class FollowerId implements Serializable {
    private Long userId;
    private Long followerId;
}
