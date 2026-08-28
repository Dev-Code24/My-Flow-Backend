package com.myflow.my_flow.dto.requests.auth;

import com.myflow.my_flow.types.AuthProvider;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class RegisteringUserDTO {
    @NotNull
    private String email, password, name;
}
