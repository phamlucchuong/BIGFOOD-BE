package com.example.BIGFOOD.mapper;


import org.mapstruct.Mapper;

import com.example.BIGFOOD.entity.Permission;

import com.example.BIGFOOD.dto.request.PermissionCreateRequest;
import com.example.BIGFOOD.dto.response.PermissionResponse;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
   Permission toPermission(PermissionCreateRequest permission);
   PermissionResponse toPermissionResponse(Permission permission);
} 