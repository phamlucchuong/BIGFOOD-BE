package com.example.bigfood.mapper;


import org.mapstruct.Mapper;

import com.example.bigfood.entity.Permission;

import com.example.bigfood.dto.request.PermissionCreateRequest;
import com.example.bigfood.dto.response.PermissionResponse;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
   Permission toPermission(PermissionCreateRequest permission);
   PermissionResponse toPermissionResponse(Permission permission);
} 