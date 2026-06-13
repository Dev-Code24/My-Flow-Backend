package com.myflow.my_flow.dto.responses.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticatedUserDTO {
    private String email;
    private String username;
    private boolean inGame;
}
