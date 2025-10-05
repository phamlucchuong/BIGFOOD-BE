package com.example.BIGFOOD.dto.request;


import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleCreateRequest {
    private String name;
    private String description;
    private Set<String> permission ; 
}
