package com.example.bigfood.dto.response;


import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    @Getter(AccessLevel.NONE) 
    @Setter(AccessLevel.NONE)
    boolean isDeleted;

    @JsonProperty("isDeleted")
    public boolean getDeleted() { return this.isDeleted;}
    public void setDeleted(boolean deleted) {this.isDeleted = deleted;}
}
