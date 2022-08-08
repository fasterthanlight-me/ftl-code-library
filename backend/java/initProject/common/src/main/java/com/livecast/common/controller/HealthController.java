package com.livecast.common.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    private final static String OK_MESSAGE = "ok";

    @GetMapping("/v1/public/health")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok().body(OK_MESSAGE);
    }
}
