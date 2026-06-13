package com.myflow.my_flow.dto.responses.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegisteredUserDTO {
    private String email;
    private String username;
}
