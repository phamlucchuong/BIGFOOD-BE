package com.example.bigfood.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.bigfood.dto.request.RoleCreateRequest;
import com.example.bigfood.dto.request.RoleUpdateRequest;
import com.example.bigfood.dto.response.RoleResponse;
import com.example.bigfood.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permission" , ignore = true)
    Role toRole(RoleCreateRequest role);
    RoleResponse toRoleResponse(Role role);
    void toUpdate (@MappingTarget Role role , RoleUpdateRequest request);
}
