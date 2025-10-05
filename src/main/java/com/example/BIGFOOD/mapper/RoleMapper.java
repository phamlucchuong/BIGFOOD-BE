package com.example.BIGFOOD.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.BIGFOOD.dto.request.RoleCreateRequest;
import com.example.BIGFOOD.dto.request.RoleUpdateRequest;
import com.example.BIGFOOD.dto.response.RoleResponse;
import com.example.BIGFOOD.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permission" , ignore = true)
    Role toRole(RoleCreateRequest role);
    RoleResponse toRoleResponse(Role role);
    void toUpdate (@MappingTarget Role role , RoleUpdateRequest request);
}
