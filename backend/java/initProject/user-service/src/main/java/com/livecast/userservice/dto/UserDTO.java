package com.livecast.userservice.dto;

import com.livecast.common.entity.Attachment;
import com.livecast.userservice.entity.enums.UserType;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Data
public class UserDTO {
    //We do not care about id validation. It would be overridden.
    private Long id;

    @NotEmpty
    //Length validation is nested from cognito since user is first created there
    private String username;

    @Length(max = 50, message = "First name length must be at max 50 characters")
    private String firstName;
    @Length(max = 50, message = "Last name length must be at max 50 characters")
    private String lastName;

    @NotEmpty
    @Email
    private String email;

    @Length(max = 300, message = "Bio length must be at max 300 characters")
    private String bio;

    //todo: add phone number validation based on regex with desired input format
    private String phoneNumber;

    @Min(0)
    @Max(120)
    private Short age;

    //We do not care about type validation. It would be overridden.
    private UserType type;
    private Attachment picture;
}
