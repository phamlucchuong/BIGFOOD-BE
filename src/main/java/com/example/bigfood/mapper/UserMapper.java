package com.example.bigfood.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.bigfood.dto.request.UserCreateRequest;
import com.example.bigfood.dto.request.UserUpdateRequest;
import com.example.bigfood.dto.response.UserResponse;
import com.example.bigfood.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreateRequest request);

    @Mapping(target = "deleted", expression = "java(user.isDeleted())")
    UserResponse toUserResponse(User user);
    
    @Mapping(target = "roles" , ignore = true)
    void toUpdate(@MappingTarget User user , UserUpdateRequest request);
}
