package com.livecast.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CustomException extends ResponseStatusException {
    public static final String NOT_FOUND = "Entity not found";
    public static final String CURRENT_USER_DOES_NOT_EXIST = "Current user does not exist";
    public static final String FORBIDDEN_OTHER_USER = "You can't change other user profile";
    public static final String FORBIDDEN_ROOM_EDIT = "You can't edit room without moderators permissions";
    public static final String ALREADY_EXIST = "Entity already exist";

    public CustomException(HttpStatus status, String reason) {
        super(status, reason);
    }
}
