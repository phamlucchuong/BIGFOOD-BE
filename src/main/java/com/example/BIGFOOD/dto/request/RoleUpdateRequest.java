package com.example.BIGFOOD.dto.request;



import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.BIGFOOD.entity.Permission;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleUpdateRequest {
    String name ;
    String description;
    Set<Permission>permissions;
}
