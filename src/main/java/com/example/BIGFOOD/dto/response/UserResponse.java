package com.example.BIGFOOD.dto.response;


import java.util.Set;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private String username;
    private String password;
    private String email;
    private Set<RoleResponse> roles;
}
