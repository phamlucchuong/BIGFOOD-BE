package com.example.bigfood.dto.response;


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
    private String id;
    private String name;
    private String phone;
    private String password;
    private String email;
    private Set<RoleResponse> roles;
}
