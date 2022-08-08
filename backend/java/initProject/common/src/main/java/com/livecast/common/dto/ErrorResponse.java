package com.livecast.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse {
    private String message;
    private List<Violation> violations = new ArrayList<>();
    private LocalDateTime timestamp = LocalDateTime.now();
}
