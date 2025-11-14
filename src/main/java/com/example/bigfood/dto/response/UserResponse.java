package com.example.bigfood.dto.response;


import java.time.LocalDateTime;
import java.util.Set;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String name;
    String phone;
    String email;
    Set<RoleResponse> roles;
    LocalDateTime createdAt;
    boolean isDeleted;
}
